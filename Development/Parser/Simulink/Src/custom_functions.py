
import logging
logging.basicConfig(level=logging.INFO)
logging.basicConfig(filename='app.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')

import json
from os.path import join, isfile
from conf import *
from neo4j import GraphDatabase
from os import listdir


def retrieve_dumped_information(path, filename):
    try:
        with open(join(path,filename)) as loadfile:
            information = json.load(loadfile)
            return information
    except:
        logging.warning('File ' + filename + ' could not be opened.')

def universal_insert(dataset, labels, id_key = 'id'):
    driver = GraphDatabase.driver(encrypted=False, uri = neo4juri, auth = (neo4jusername, neo4jpassword))
    # dataset is a list of dicts that shall be inserted as single nodes, the depth of the dict is 1
    # labels is a string like ':label1:label2'
    # idkey is the dict key which shall be used as unique property for the merge clause. shall be a string.
    k=0
    for node in dataset:
        insert_query = "merge(node%s {id: \"%s\"})\n"%(labels, node[id_key])
        properties_for_insert_query = build_properties_for_insert_query(node, 'node')
        insert_query                = insert_query + properties_for_insert_query

        logging.debug(insert_query)
        logging.debug(labels, k)
        k = k + 1
        with driver.session() as session:
            session.run(insert_query)
            
def build_properties_for_insert_query(data,cypher_node_handle, preposition=''):
    # function that builds all additional set_properties lines
    # inputs: data: a single json object that shall be transformed into a node
    # cypher_node_handle: the name used in the cypher query for the node
    properties_for_insert_query = ''
    propertykeys = list(data.keys())
    for propertykey in propertykeys:
        if type(data[propertykey]) == dict:
            properties_from_nested_dict = build_properties_for_insert_query(data[propertykey],cypher_node_handle, preposition=propertykey + '_')
            properties_for_insert_query = properties_for_insert_query + properties_from_nested_dict
        else:
            clean_property              = transform_to_string_and_insert_quotes(data[propertykey])
            data[propertykey]           = clean_property
            additionalproperty          = "set %s.%s=\"%s\"\n"%(cypher_node_handle, preposition + propertykey, str(data[propertykey]))
            properties_for_insert_query = properties_for_insert_query + additionalproperty
    return properties_for_insert_query