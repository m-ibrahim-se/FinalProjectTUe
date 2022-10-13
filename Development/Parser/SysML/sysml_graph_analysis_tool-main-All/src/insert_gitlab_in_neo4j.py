from conf import *
from crown_jewels import *
from custom_functions import *
from neo4j import GraphDatabase
#%% Resources

# https://www.programcreek.com/python/example/92749/neo4j.v1.GraphDatabase.driver

# https://gist.github.com/tomasonjo/52d231a7e18c1a24aaa18e81764bda44
#%%
driver = GraphDatabase.driver(neo4juri, auth = (neo4jusername, neo4jpassword),encrypted=False)



#%% Definitions

full_members = retrieve_dumped_information(gitlab_path, 'data_file_full_members_json_list.json')
universal_insert(full_members, ':person:gitlab', id_key='id')


group_members = retrieve_dumped_information(gitlab_path, 'data_file_group_members_json_list.json')
universal_insert(group_members, ':person:gitlab', id_key='id')


full_projects = retrieve_dumped_information(gitlab_path, 'data_file_full_projects_json_list.json')
universal_insert(full_projects, ':gitlabproject:meta:gitlab', id_key='id')


projects_issues = retrieve_dumped_information(gitlab_path, 'data_file_projects_issues_json_list.json')
universal_insert(projects_issues, ':issue:information:gitlab', id_key='id')


issues_notes = retrieve_dumped_information(gitlab_path, 'data_file_issues_notes_json_list.json')
universal_insert(issues_notes, ':note:information:gitlab', id_key='id')



merge_requests = retrieve_dumped_information(gitlab_path, 'data_file_project_merge_requests_json_list.json')
universal_insert(merge_requests, ':merge_request:information:gitlab', id_key='id')


mr_notes = retrieve_dumped_information(gitlab_path, 'data_file_merge_request_notes_json_list.json')
universal_insert(mr_notes, ':note:information:gitlab', id_key='id')


commits = retrieve_dumped_information(gitlab_path, 'data_file_projects_commits_json_list.json')
universal_insert(commits, ':commit:information:gitlab', id_key='id')


files = retrieve_dumped_information(gitlab_path, 'data_file_decoded_files_json_list.json')
universal_insert(files, ':file:implementation:gitlab', id_key='blob_id')



#%% make web urls
create_commit_urls="""
match(c:commit)
match(p:gitlabproject)
where c.project_id=p.id
set c.web_url=p.web_url+'/-/commit/'+c.id
return count(c)
"""

create_file_urls="""
match(f:file)
match(p:gitlabproject)
where p.id=f.project_id
set f.web_url=p.web_url+'/-/blob/'+f.ref+'/'+f.file_path
return count(f), f.web_url limit 20
"""

create_notes_urls="""
match(n:note)-[:belongs_to]->(entity)
set n.web_url=entity.web_url
return count(n)
"""


#%% Running functions

with driver.session() as session:
    created_commit_urls = session.run(create_commit_urls)
    created_file_urls = session.run(create_file_urls)
    created_notes_urls = session.run(create_notes_urls)
