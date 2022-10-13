#%% import libraries
from os.path import join
import json
from neo4j import GraphDatabase
from custom_functions import *
from conf import *
from crown_jewels import *


#%% load data

messages = retrieve_dumped_information(slack_path, 'messages.json')
users = retrieve_dumped_information(slack_path,'users.json')
files = retrieve_dumped_information(slack_path,'files.json')
channels = retrieve_dumped_information(slack_path,'channels.json')
threads = retrieve_dumped_information(slack_path,'threads.json')


#%%

universal_insert(dataset= messages, labels=':message:information:slack', id_key='id')
universal_insert(dataset= users, labels=':person:slack', id_key='id')
universal_insert(dataset= files, labels=':file:slack', id_key='id')
universal_insert(dataset= channels, labels=':meta:slack', id_key='id')
universal_insert(dataset = threads, labels=':thread:message:information:slack', id_key='id')
