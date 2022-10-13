from neo4j import GraphDatabase
#%% Resources

# https://www.programcreek.com/python/example/92749/neo4j.v1.GraphDatabase.driver

# https://gist.github.com/tomasonjo/52d231a7e18c1a24aaa18e81764bda44
#%%
uri = "bolt://localhost:7687"
driver = GraphDatabase.driver(uri, auth = ("neo4j", "asdf"),encrypted=False)
#%%
def relate_emails_and_authors():
    # makes a MATCH between the specified email property and the name of the person. the relation
    # based on the similarity between their names WITH variations between
    # first name and last name and taking into account and that most people
    # have their name in their mail address
    query="""
    MATCH(e:email)
        MATCH(p:person)
          WITH e, p, SPLIT(e.MessageFromName,' ') AS messengersplitname, SPLIT(p.name, ' ') AS personsplitname
            WITH e,p, personsplitname, apoc.text.clean(messengersplitname[0]) AS messengercleanname0, apoc.text.clean(messengersplitname[1]) AS messengercleanname1
            with e,p,personsplitname, messengercleanname1 + messengercleanname0 as messengercleannamevar0
                WHERE apoc.text.distance(personsplitname[0], messengercleanname0) + apoc.text.distance(personsplitname[1], messengercleanname1) < 5 OR apoc.text.distance(personsplitname[1], messengercleanname0) + apoc.text.distance(personsplitname[0], messengercleanname1) < 5 OR apoc.text.clean(e.MessageFromName) contains(apoc.text.clean(p.name)) OR messengercleannamevar0 CONTAINS(apoc.text.clean(p.name))
                    MERGE (p)-[:authored]->(e)
                    RETURN distinct apoc.text.clean(e.MessageFromName)
    """
    with driver.session() as session:
        result = session.run(query)
    for key in result:
        print(key)
    return result

def get_additional_email_addresses_of_authors():
    # takes mailaddresses from email_senders and adds them to the person's
    # property `emailaddress` if they are not already part of emailaddress.
    query="""
    call apoc.periodic.iterate("
      MATCH(p:person)-[:authored]->(e:email)
      RETURN p,e
      ","
      WHERE  NOT p.emailaddress CONTAINS e.MessageFromEmail AND e.MessageFromEmail CONTAINS '@'
        SET p.emailaddress=p.emailaddress+','+e.MessageFromEmail",{batchSize:1})
    """
    with driver.session() as session:
        result = session.run(query)
    for key in result:
        print(key)
    return result

def relate_emails_and_other_persons(emailmetadata='MessageCCEmail', relation='(email)-[:CC]->(p)'):

    query="""
    MATCH(e:email)
    MATCH(p:person)
    WITH collect(e) AS emailslist, p
    UNWIND range(0,size(emailslist)) AS k
      WITH p, emailslist[k] AS email, split(emailslist[k].%s,',') AS CClist
      WHERE size(CClist)>0
        unwind range(0,size(CClist)) AS l
          WITH p, email, CClist
            WHERE apoc.text.clean(p.emailaddress) contains apoc.text.clean(CClist[l])
              MERGE %s
   """%(emailmetadata, relation)
    with driver.session() as session:
        result = session.run(query)
    for key in result:
        print(key)
    return result

#%%

relate_emails_and_authors()
get_additional_email_addresses_of_authors()
relate_emails_and_other_persons()
relate_emails_and_other_persons(emailmetadata='MessageToEmail', relation='(email)-[:sent_to]->(p)')
