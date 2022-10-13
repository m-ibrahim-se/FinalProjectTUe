from neo4j import GraphDatabase

#%% setup connection
uri = "bolt://localhost:7687"
driver = GraphDatabase.driver(uri, auth = ("neo4j", "asdf"),encrypted=False)

#%% define queries

relate_messages_and_channels="""MATCH(c:slack:meta)
MATCH(m:message)
WHERE m.channel = c.id
MERGE (c)-[:contains]->(m)
"""

relate_messages_and_threads="""
MATCH(cs:slack:meta)
UNWIND cs AS Channel
    MATCH(msgs:message)
    WHERE EXISTS {(msgs)<-[:contains]-(Channel)} AND msgs.thread_ts = msgs.ts
    UNWIND msgs as msg
        CALL{
            WITH msg, Channel
            MATCH(thrd {thread_ts:msg.ts})
            WITH thrd, Channel ORDER BY thrd.ts
            WITH collect(thrd.ts) AS thrdtimeline, COUNT(thrd) AS nom, Channel
            FOREACH(i IN RANGE(1,nom-1)|
                MERGE(prvsmsg:message{ts:thrdtimeline[i-1], channel:Channel.id})
                MERGE(nxtmsg:message:thread{ts:thrdtimeline[i], channel: Channel.id})
                MERGE(prvsmsg)<-[rel:follows]-(nxtmsg)
                set rel.delta_t = toFloat(nxtmsg.ts)-toFloat(prvsmsg.ts)
                )
                RETURN nom
              }
              RETURN nom
"""

relate_users_and_messages="""MATCH(p:person:slack)
MATCH(m:message)
WHERE m.user = p.id OR m.comment_user = p.id
MERGE(p)-[:authored]->(m)
return count(m)
"""

relate_users_and_files="""
MATCH(p:person:slack)
MATCH(f:file:slack)
WHERE f.user = p.id
MERGE(p)-[:authored]->(f)
"""

relate_files_and_channels="""
MATCH(c:slack:meta)
MATCH(f:file)
WHERE f.channels contains c.id
MERGE (c)-[:contains]->(f)
"""

relate_mentions_and_users="""
MATCH(p:person:slack)
MATCH(m:message)
WHERE m.text contains p.id
MERGE (p)-[:mentioned_in]->(m)
"""


relate_slackusers_and_persons="""
MATCH(p:person)
MATCH(u:person:slack)
WHERE p <> u
  WITH p,u, apoc.text.clean(toString(p.name)) AS pcleanname, apoc.text.clean(toString(u.profile_real_name) AS ucleanname, apoc.text.clean(p.username) AS pcleanusername
      WHERE p.email CONTAINS u.profile_email OR pcleanname CONTAINS ucleanname OR pcleanusername CONTAINS ucleanname
        CALL apoc.refactor.mergeNodes([p,u],{properties:"combine", MERGERels:true})
        YIELD node
RETURN node

"""
relate_files_and_messages="""
MATCH(f:file:slack)
MATCH(m:message:slack)
WHERE f.id = m.connected_files_id OR m.text CONTAINS f.id
MERGE (m)-[:mentions]->(f)
"""

create_message_chains="""
MATCH(cs:slack:meta)
UNWIND cs AS Channel
        CALL{
            WITH Channel
            MATCH(msgs {channel:Channel.id}) WHERE NOT msgs:thread
            WITH msgs, Channel ORDER BY msgs.ts
            WITH collect(msgs.ts) AS msgstimeline, COUNT(msgs) AS nom, Channel
            FOREACH(i IN RANGE(1,nom-1)|
                MERGE(prvsmsg:message{ts:msgstimeline[i-1], channel:Channel.id})
                MERGE(nxtmsg:message{ts:msgstimeline[i], channel: Channel.id})
                MERGE(prvsmsg)<-[rel:follows]-(nxtmsg)
                set rel.delta_t = toFloat(nxtmsg.ts)-toFloat(prvsmsg.ts)
                )
                RETURN nom
              }
              RETURN nom
"""


#%% execute queries

with driver.session() as session:
    result = session.run(relate_messages_and_channels)
    result = session.run(relate_messages_and_threads)
    result = session.run(relate_users_and_messages)
    result = session.run(relate_users_and_files)
    result = session.run(relate_files_and_channels)
    result = session.run(relate_mentions_and_users)
    #result = session.run(relate_slackusers_and_persons)
    result = session.run(relate_files_and_messages)
    result = session.run(create_message_chains)
