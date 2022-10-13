from conf import *
from custom_functions import *
from crown_jewels import *
from neo4j import GraphDatabase
#%% Resources

# https://www.programcreek.com/python/example/92749/neo4j.v1.GraphDatabase.driver

# https://gist.github.com/tomasonjo/52d231a7e18c1a24aaa18e81764bda44
#%%

driver = GraphDatabase.driver(  neo4juri, auth=(neo4jusername, neo4jpassword), encrypted=False)

relate_authors_with_information="""
    match(a:person)
    match(b:information)
    where a.id = b.author_id or a.id = b.created_by_id
    merge (a)-[rel:authored]->(b)
    return count(a), count(b), count(rel)
"""

relate_notes_and_issues="""
    match(n:note)
    match(i:issue)
    where n.noteable_id = i.id
    merge (n)-[rel:belongs_to]->(i)
    return count(n), count(i), count(rel)
"""

relate_notes_and_merge_requests="""
    match(n:note)
    match(mr:merge_request)
    where n.noteable_id = mr.id
    merge (n)-[rel:belongs_to]->(mr)
    return count(n), count(mr), count(rel)
""" 

relate_mergers_with_mrs="""
    match(a:person)
    match(mr:merge_request)
    where a.id = mr.merged_by_id
    merge (a)-[:merged]->(mr)
    return count(a)
"""

relate_closers_with_information="""
    match(a:person)
    match(i:information)
    where a.id = i.closed_by_id
    merge (a)-[:closed]->(i)
    return count(a)
"""

relate_assignees_with_information="""
    match(a:person)
    match(i:information)
    where a.id = i.assignee_id
    merge (a)-[:assigned_to]->(i)
    return count(a)
"""

relate_projects_with_everything="""
    match(p:gitlabproject)
    match(n)
    where n.project_id = p.id
    merge (n)-[rel:is_part_of]->(p)
    return(count(rel))
"""

relate_commits_with_authors="""
    match(a:person)
    match(c:commit)
    where apoc.text.distance(a.name, c.author_name)<3 OR a.emailaddress contains c.author_email
    merge (a)-[:authored]->(c)
    return count(a)
"""


add_committer_emails_to_persons_1="""
    match(a:person)-[:authored]->(c:commit)
    where not exists(a.emailaddress)
    set a.emailaddress=c.author_email
"""
add_committer_emails_to_persons_2="""
call apoc.periodic.iterate("
    match(a:person)-[:authored]->(c:commit)
    return a,c
    ","
    where not a.emailaddress contains c.author_email
    set a.emailaddress=a.emailaddress+','+c.author_email",
    {batchSize:1})
"""
relate_commits_with_parents="""
    match (c:commit)
    match (c2:commit)
    where c.id = c2.parent_id1 or c.id=c2.parent_id2
    merge (c)-[:builds_on]->(c2)
"""

relate_commits_with_files="""
    match(c:commit)
    match(f:file)
    where c.id = f.last_commit_id or c.id=f.commit_id
    merge(c)-[:updates]->(f)
    return count(c), count(f), c, f limit 50
"""

add_file_endings="""
    match(f:file)
    with f, split(f.file_name,'.')[-1] as file_ending
    set f.file_ending=file_ending
    return distinct file_ending
"""

classify_files="""
call apoc.load.csv('file:///file_endings_list.txt') yield map
with map.node_type as node_type, map.ending as file_ending
match(f:file)
where f.file_ending=file_ending
set f.node_type=node_type
return  count(f)
"""

relate_files_and_commits="""
call apoc.periodic.iterate("
    call apoc.load.json('file:///gitlab/data_file_files_commits_json_list.json') yield value as c
    return c
    ","
    match(f:file)
    match(commits:commit)
    where c.id = commits.id AND f.file_path = c.file_path AND f.project_id = c.project_id
    merge(commits)-[:updates]->(f)
    ",{batchSize: 500, iterateList: true})
"""

#%% work magic
with driver.session() as session:
    session.run(relate_authors_with_information)
    session.run(relate_notes_and_issues)
    session.run(relate_notes_and_merge_requests)
    session.run(relate_mergers_with_mrs)
    session.run(relate_closers_with_information)
    session.run(relate_assignees_with_information)
    session.run(relate_projects_with_everything)
    session.run(relate_commits_with_authors)
    session.run(relate_commits_with_parents)
    session.run(relate_commits_with_files)
    session.run(add_file_endings)
    session.run(classify_files)
    #session.run(relate_blames_and_commits)
    session.run(add_committer_emails_to_persons_1)
    session.run(add_committer_emails_to_persons_2)