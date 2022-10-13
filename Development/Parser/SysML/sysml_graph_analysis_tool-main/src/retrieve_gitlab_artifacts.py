import gitlab # pip install python-gitlab
import base64
from conf import *
from custom_functions import *
from crown_jewels import *



#%%
# This script has the intent of retrieving all artifacts of the emps development
# from the gitlab api and storing them in json files.
# The files shall afterwards be pushed into the graph database, where relations
# shall be made.
# To enable relations between the artifacts, the relations have to be
# traceable in the artifacts themselves.
# this script builds upon the following resources:
# https://python-gitlab.readthedocs.io/en/stable/gl_objects/groups.html
# https://docs.gitlab.com/ee/api/api_resources.html
# https://docs.gitlab.com/ee/api/groups.html

#%%
# get the group object for EMPS and IDs of members and projects
def retrieve_group_projects_members(gitlab_group_keyword):
    gitlab_group=gl.groups.list(search=gitlab_group_keyword)[0]
    group_projects = gitlab_group.projects.list(all = True)
    group_members = gitlab_group.members.list(all = True)

    return(gitlab_group,group_projects,group_members)


def retrieve_full_projects(group_projects):
    full_projects = []
    for i in range(len(group_projects)):
        # get the ID of the project
        project_id = group_projects[i].id
        # get the full project object from gitlab
        full_project = gl.projects.get(project_id)
        # append it to the list full_projects
        full_projects.append(full_project)

    # return list with full projects
    return(full_projects)


def retrieve_full_members(group_members):
    full_members = []
    for i in range(len(group_members)):
        # get id of the member
        member_id = group_members[i].id
        # get full object of member
        full_member = gl.users.get(member_id)
        # append to full_members
        full_members.append(full_member)

    return(full_members)


def retrieve_issues_from_projects(full_projects):
    projects_issues = []
    for i in range(len(full_projects)):
        # retrieve issues of the project
        project_issues = full_projects[i].issues.list(all = True)
        # append issues to the list with issues from all projects
        projects_issues.append(project_issues)
    projects_issues = unpack_gitlab_objects_from_nested_lists(projects_issues)

    return(projects_issues)


def retrieve_notes_on_issues(projects_issues):
    issues_notes = []
    for i in range(len(projects_issues)):
        project_issue = projects_issues[i]
        # retrieve notes of the issue
        issue_notes = project_issue.notes.list(all = True)
        # append notes to the list with all notes
        issues_notes.append(issue_notes)
        issues_notes = unpack_gitlab_objects_from_nested_lists(issues_notes)

    return(issues_notes)


def retrieve_projects_merge_requests(full_projects):
    projects_merge_requests = []
    for i in range(len(full_projects)):
        # retrieve merge requests of the project
        project_merge_requests = full_projects[i].mergerequests.list(all = True)
        # append to projects_mergerequests
        projects_merge_requests.append(project_merge_requests)
    projects_merge_requests = unpack_gitlab_objects_from_nested_lists(projects_merge_requests)

    return projects_merge_requests


def retrieve_notes_on_merge_requests(projects_merge_requests):
    mrs_notes = []
    for i in range(len(projects_merge_requests)):
        merge_request = projects_merge_requests[i]
        # retrieve notes of the MR
        mr_notes = merge_request.notes.list(all = True)
        # append notes to the list with all notes
        mrs_notes.append(mr_notes)
        mrs_notes = unpack_gitlab_objects_from_nested_lists(mrs_notes)

    return(mrs_notes)


def retrieve_filetree_from_projects(full_projects):
    projects_filetree = []
    for i in range(len(full_projects)):
        # retrieve filetree of single project from gitlab
        project_filetree = full_projects[i].repository_tree(recursive = True, all = True)
        # append to projects_filetree
        projects_filetree.append(project_filetree)

    return(projects_filetree)


def retrieve_files_from_projects(projects_filetree,full_projects):
    projects_files = []
    for i in range(len(projects_filetree)):
        project_filetree = projects_filetree[i]
        full_project = full_projects[i]
        for j in range(len(project_filetree)):
            filetree_object = project_filetree[j]
            file_type = filetree_object['type']
            file_path = filetree_object['path']
            if file_type != 'tree':
                file_object = full_project.files.get(file_path = file_path,ref = 'master')
                projects_files.append(file_object)

    return(projects_files)



def retrieve_commits_from_projectsfiles(projects_filetree,full_projects):
    commits_count = 0
    files_commits = []
    for i in range(len(projects_filetree)):
        project_filetree = projects_filetree[i]
        full_project = full_projects[i]
        for j in range(len(project_filetree)):
            filetree_object = project_filetree[j]
            file_type = filetree_object['type']
            file_path = filetree_object['path']
            if file_type != 'tree':
                commit_object = full_project.commits.list(file_path=file_path, all = True)
                commit_object_stripped_unwound = unwind_to_json_list(commit_object)
                for k in range(len(commit_object)):
                    commit_object_stripped_unwound[k]['file_path'] = file_path
                    commit_object_stripped_unwound[k]['project_id'] = full_project.attributes['id']
                    commits_count = commits_count + 1
                    print(commits_count)
                    files_commits.append(commit_object_stripped_unwound[k])

    return(files_commits)


def retrieve_mergerequests_from_group(gitlab_group):
    group_merge_requests = []
    # get merge requests as list
    group_merge_requests = gitlab_group.mergerequests.list(all = True)


    return(group_merge_requests)


def retrieve_commits_from_projects(full_projects):
    projects_commits = []
    for i in range(len(full_projects)):
        # get a project from the projects list
        full_project = full_projects[i]
        # get the commits of project i
        project_commits = full_project.commits.list(all = True)
        # append to the projects_commits list
        projects_commits.append(project_commits)

    projects_commits = unpack_gitlab_objects_from_nested_lists(projects_commits)

    return(projects_commits)



#%%

def strip_to_json(gitlabobject):
    gitlabdictobject = {}
    try:
        attributes = gitlabobject.attributes
        gitlabdictobject = attributes
    except:
        print('something went wrong with this:')
        print(type(gitlabobject))
    try:
        _parent_attrs = gitlabobject._parent_attrs
        gitlabdictobject['_parent_attrs'] = _parent_attrs
    except:
        print('no parent artifacts')
    try:
        gitlabdictobject['objecttype'] = str(type(gitlabobject))
    except:
        print('could not determine type')

    return(gitlabdictobject)

def check_for_gitlab_objecttype(object):
    if 'gitlab' in str(type(object)):
        return True

    else: return False

def unpack_gitlab_objects_from_nested_lists(gitlabobjects):
    unpackedgitlabobjectslist = []
    for j in range(len(gitlabobjects)):
        if check_for_gitlab_objecttype(gitlabobjects[j]) == True:
            unpackedgitlabobjectslist.append(gitlabobjects[j])
        else:
            for k in range(len(gitlabobjects[j])):
                if check_for_gitlab_objecttype(gitlabobjects[j][k]) == True:
                        unpackedgitlabobjectslist.append(gitlabobjects[j][k])

    return(unpackedgitlabobjectslist)

def unwind_to_json_list(gitlabobjects_list):
    objects_json_list = []
    for k in range(len(gitlabobjects_list)):
        objectasjson = strip_to_json(gitlabobjects_list[k])
        if objectasjson["objecttype"] == "<class 'gitlab.v4.objects.ProjectCommit'>":
            parent_ids = objectasjson["parent_ids"]
            try:
                objectasjson["parent_id1"] = parent_ids[0]
            except:
                pass
            try:
                objectasjson["parent_id2"] = parent_ids[1]
            except:
                pass
        objects_json_list.append(objectasjson)

    return(objects_json_list)


def decode_file_contents(gitlab_files_json_list):
    for i in range(len(gitlab_files_json_list)):
        gitlabfile_json_object = gitlab_files_json_list[i]
        # decode content as specified in https://python-gitlab.readthedocs.io/en/stable/gl_objects/projects.html
        decodable_content = gitlabfile_json_object["content"]
        # decode content
        decoded_content = str(base64.b64decode(decodable_content))

        gitlab_files_json_list[i]["decoded_content"] = decoded_content

    return gitlab_files_json_list



#%% run

# creating a gitlab object:
gl = gitlab.Gitlab(gitlaburl, private_token = gitlabtoken)

(group,group_projects,group_members) = retrieve_group_projects_members(gitlab_group_keyword)

group_merge_requests    = retrieve_mergerequests_from_group(group)

full_members            = retrieve_full_members(group_members)

full_projects           = retrieve_full_projects(group_projects)
#%%
projects_issues         = retrieve_issues_from_projects(full_projects)

projects_filetree       = retrieve_filetree_from_projects(full_projects)
#%%
projects_merge_requests = retrieve_projects_merge_requests(full_projects)

files                   = retrieve_files_from_projects(projects_filetree = projects_filetree, full_projects = full_projects)

projects_commits        = retrieve_commits_from_projects(full_projects)

issues_notes            = retrieve_notes_on_issues(projects_issues)

merge_request_notes     = retrieve_notes_on_merge_requests(projects_merge_requests)

files_commits           = retrieve_commits_from_projectsfiles(projects_filetree,full_projects)

list_of_filenames = [
    "data_file_full_projects_json_list.json",
    "data_file_full_members_json_list.json",
    "data_file_group_members_json_list.json",
    "data_file_group_projects_json_list.json",
    "data_file_projects_issues_json_list.json",
    "data_file_decoded_files_json_list.json",
    "data_file_group_merge_requests_json_list.json",
    "data_file_project_merge_requests_json_list.json",
    "data_file_issues_notes_json_list.json",
    "data_file_merge_request_notes_json_list.json",
    "data_file_projects_commits_json_list.json",
    "data_file_files_commits_json_list.json"]

list_of_data = [
full_projects,
full_members,
group_members,
group_projects,
projects_issues,
files,
group_merge_requests,
projects_merge_requests,
issues_notes,
merge_request_notes,
projects_commits,
files_commits]


for k in range(len(list_of_data)-1):
    data_item = unwind_to_json_list(list_of_data[k])

    if list_of_filenames[k] == "data_file_decoded_files_json_list.json":
        data_item = decode_file_contents(data_item)

    dump_retrieved_information(path = gitlab_path, filename = list_of_filenames[k], information = data_item)

data_item = list_of_data[-1]
dump_retrieved_information(path = gitlab_path, filename = list_of_filenames[-1], information = data_item)
