import os
import json
import sys
import shutil
from conf import *
import Neo4j_Query as neoQ
import retrieve_Simulink_model as rSimModel


##########################################################
##########            Simulink Models            ##########
##########################################################


def move_files(model_type=None):
    # Simulink
    current_dir_name = os.path.dirname(__file__)
    root_dir = os.path.abspath(os.path.join(current_dir_name, os.pardir))
    parsed_data_file_path = os.path.join(root_dir, "ParsedDataFiles")

    # Copy files
    source_folder = parsed_data_file_path
    destination_folder = os.path.join(neo4jfileimportpath, simulinkfolder)

    # remove all files from destination path
    if os.path.exists(destination_folder):
        for root, dirs, files in os.walk(destination_folder, topdown=False):
            for file in files:
                os.remove(os.path.join(root, file))

            # Add this block to remove folders
            for dir in dirs:
                os.rmdir(os.path.join(root, dir))
    else:
        os.mkdir(destination_folder)

    # fetch all files
    for file_name in os.listdir(source_folder):
        # construct full file path
        source = os.path.join(source_folder, file_name)
        destination = os.path.join(destination_folder, file_name)

        # copy only files
        if os.path.isfile(source):
            shutil.copy(source, destination)
            print('Moved:', file_name)
        else:
            print('This is a folder. So no copy..')


def insert_file_data(model_type=None, elementType=None):
    # Simulink
    import_folder = os.path.join(neo4jfileimportpath, simulinkfolder)

    conn = neoQ.Neo4jConnection(neo4juri, neo4jusername, neo4jpassword)

    # fetch all files
    for file_name in os.listdir(import_folder):
        # construct full file path
        sourcefile = os.path.join(import_folder, file_name)
        # Insert only files
        if os.path.isfile(sourcefile):
            file_path = simulinkfolder.replace("\\", "/")
            if elementType == 'node':
                insert_query = "CALL apoc.load.json('file:///" + file_path + "/" + file_name + "') YIELD value as records " \
                                                                                               "WHERE records.type = 'node' " \
                                                                                               "UNWIND records as row " \
                                                                                               "CALL apoc.merge.node(row.labels, {id:row.id}, row.properties, {}) YIELD node return node;"
            if elementType == 'relationship':
                insert_query = "CALL apoc.load.json('file:///" + file_path + "/" + file_name + "') YIELD value as records " \
                                                                                               "WHERE records.type = 'relationship' " \
                                                                                               "MATCH (src) " \
                                                                                               "WHERE src.id = records.start.id " \
                                                                                               "MATCH (dst) " \
                                                                                               "WHERE dst.id = records.end.id " \
                                                                                               "CALL apoc.merge.relationship(src, records.label, records.properties, {}, dst, {}) YIELD rel return rel;"
            # print("Query String : " + insert_query)
            query_result = conn.query(insert_query, db=neo4jdbname)
            # print(query_result)
            # [<Record file='file:///Simulink/ParsedDataFiles/ReachCoreach_Cover.json' source='file' format='json' nodes=91 relationships=120 properties=735 time=504 rows=211 batchSize=-1 batches=0 done=True data=None>]
            # otherwise None
            print(file_name + ": " + elementType + " loaded successfully into database")
    conn.close()


def execute_query(query):
    conn = neoQ.Neo4jConnection(neo4juri, neo4jusername, neo4jpassword)
    given_query = query
    print("Query String : " + given_query)
    query_result = conn.query(given_query, db=neo4jdbname)
    # print(query_result)
    print("Query executed successfully")
    conn.close()


if __name__ == '__main__':
    # Generate data file(s)
    isDeleteGraphData = True
    file_count = rSimModel.main_function()
    if file_count > 20:
        print(file_count, " files generated to load")
        move_files('Simulink')
        if isDeleteGraphData:
            # delete existing data
            execute_query("MATCH (n) DETACH DELETE n")

        # Firstly, load all node data first
        insert_file_data('Simulink', 'node')
        # Secondly, load all relationship data
        insert_file_data('Simulink', 'relationship')
    else:
        print("No file found!!!!")
