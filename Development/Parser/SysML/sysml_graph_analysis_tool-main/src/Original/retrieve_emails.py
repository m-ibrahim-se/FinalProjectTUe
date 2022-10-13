# task of the script is to extract information from emails and parse them into json

# source https://medium.com/@justinboylantoomey/fast-text-extraction-with-python-and-tika-41ac34b0fe61
#%%
from tika import parser

from os import listdir
from os.path import isfile, join
from conf import *
from custom_functions import *



#%% functions only necessary for parsing emails:



def parse_emails(list_of_email_file_names):
    list_of_emails = []
    for mail in list_of_email_file_names:
        email_data = parser.from_file(mails_path+mail)
        list_of_emails.append(email_data)
    list_of_emails = flatten_entry_of_dictionary(list_of_emails, 'metadata')
    list_of_emails = rebuild_resourceName(list_of_emails)

    return list_of_emails

def rebuild_resourceName(list_of_emails):
    for k in range(len(list_of_emails)):
        list_of_emails[k]['resourceName'] = list_of_email_file_names[k]

    return list_of_emails


#%% run code
list_of_email_file_names = retrieve_list_of_file_names(mails_path)

list_of_emails = parse_emails(list_of_email_file_names)

dump_retrieved_information(mails_path, "emails_list.json", list_of_emails)
