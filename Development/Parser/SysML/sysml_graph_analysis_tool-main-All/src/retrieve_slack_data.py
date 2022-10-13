# resources
# https://towardsdatascience.com/python-and-slack-a-natural-match-60b136883d4d
# https://api.slack.com/methods
# https://github.com/os/slacker
# https://api.slack.com/messaging/retrieving

from slacker import Slacker
import json
import os
from time import sleep
from conf import *
from custom_functions import *
from crown_jewels import *


# Authenticate with slacker
slack = Slacker(slacktoken)

#%% function definitions

def get_messages():
    messages=[]
    l = 0
    r = slack.conversations.list()
    channels = r.body ['channels']
    # Iterate through channels
    for c in channels:
        c['history']=slack.conversations.history(c['id'], limit=100).body
        channel_messages=c['history']['messages']
        for message in channel_messages:
            response = slack.chat.get_permalink(channel = c['id'], message_ts = message['ts'])
            message['web_url'] = response.body['permalink']
            message['id']=c['id']+message['ts']
            message['channel']=c['id']
            if 'files' in message:
                message['connected_files_id']=''
                for file in message['files']:
                    message['connected_files_id']= message['connected_files_id'] + ',' + file['id']
                del message['files']
            messages.append(message)

        while 'response_metadata' in c['history']:
            next_cursor=c['history']['response_metadata']['next_cursor']
            c['history']=slack.conversations.history(c['id'], limit=100, cursor=next_cursor).body
            channel_messages=c['history']['messages']
            for message in channel_messages:
                response = slack.chat.get_permalink(channel = c['id'], message_ts = message['ts'])
                message['web_url'] = response.body['permalink']
                message['id']=c['id']+message['ts']
                message['channel']=c['id']
                if 'files' in message:
                    message['connected_files_id']=''
                    for file in message['files']:
                        message['connected_files_id']= message['connected_files_id'] + ',' + file['id']
                    del message['files']
                messages.append(message)

    return messages

def get_threads(messages):
    threads=[]
    k=0
    for message in messages:
        if 'thread_ts' in message:
            k += 1
            print(k)
            channel = message['channel']
            ts=message['ts']
            thread_object = slack.conversations.replies(channel,ts).body
            sleep(0.5)
            thread = thread_object['messages'][1:]
            if 'next_cursor' in thread_object:
                print('fuck')
            for reply in thread:
                ts=reply['ts']
                reply['channel'] = channel
                reply['id'] = channel + ts
                reply['web_url'] = message['web_url']
                threads.append(reply)

    return threads

def get_channels():
    r = slack.conversations.list()
    channels = r.body ['channels']

    return channels


def get_files(channels):
    files=[]
    l = 0
    for c in channels:
        r=slack.files.list(count=100, channel=c['id'])
        for k in range(r.body['paging']['pages']):
            r=slack.files.list(count=100, channel=c['id'], page=k)
            for file in r.body['files']:
                files.append(file)
                l = l + 1
                print("files found: %d"%l)
    return files


def get_users():
    users = slack.users.list().body['members']

    return users


def save_files(users,messages,files,channels):
    with open(os.path.join('slack','messages.json'),'w') as dumpfile:
        json.dump(messages, dumpfile, indent = 4, sort_keys = True)

    with open(os.path.join('slack','users.json'),'w') as dumpfile:
        json.dump(users, dumpfile, indent = 4, sort_keys = True)

    with open(os.path.join('slack','files.json'),'w') as dumpfile:
        json.dump(files, dumpfile, indent = 4, sort_keys = True)

    with open(os.path.join('slack','channels.json'),'w') as dumpfile:
        json.dump(channels, dumpfile, indent = 4, sort_keys = True)

    with open(os.path.join('slack','threads.json'),'w') as dumpfile:
        json.dump(threads, dumpfile, indent = 4, sort_keys = True)
#%% function calls
messages = get_messages()
channels = get_channels()
files    = get_files(channels)
users    = get_users()
threads  = get_threads(messages)

list_of_data = [messages, users, files, channels, threads]
list_of_filenames=['messages.json','users.json','files.json','channels.json','threads.json']
for k in range(len(list_of_data)):
    dump_retrieved_information(path = slack_path, filename = list_of_filenames[k], information = list_of_data[k])
