#%%
from neo4j import GraphDatabase
from urllib import parse
from conf import *
from custom_functions import *
from crown_jewels import *



driver = GraphDatabase.driver(  neo4juri, auth=(neo4jusername, neo4jpassword), encrypted=False)


#%% insert 2 additional " around each " in the whole json file
def prepare_strings_for_cypher(emails_list):
    cleaned_emails_list = emails_list
    for k in range(len(emails_list)):
        print(str(emails_list[k]['resourceName']))
        mail = emails_list[k]
        for item in mail:
            itemcontent = mail[item]
            string=transform_to_string_and_insert_quotes(itemcontent)
            cleaned_emails_list[k][item] = string
        print('cleaned %d mails metadata'%(k))
        cleaned_emails_list[k]['content'] = transform_to_string_and_insert_quotes(emails_list[k]['content'])
        print('cleaned %d mails content'%(k))
    return cleaned_emails_list


#%%

def create_emails_urls(cleaned_emails_list,initial_path='https://webdisk.ads.mwn.de/Default.aspx?file=Home%2FPA%2Fdata_directory_emps%2F'):
    # craetes file urls for ewb storage systems like NAS
    for mail in cleaned_emails_list:
        name = mail['resourceName']
        parsed_name=parse.quote(name, encoding = None, errors = None)
        mail['web_url']            = initial_path + parsed_name
    return cleaned_emails_list

#%% Cypher Query to insert each email as its own
def insert_single_mail(cleaned_emails_list):
    for k in range(len(cleaned_emails_list)):
        propertykeys         = list(cleaned_emails_list[k].keys())
        propertykeys_cleaned = []
        for l in range(len(propertykeys)):
            if propertykeys[l] == 'content':
                pass
            else:
                cleaned_key = ''
            for character in propertykeys[l]:
                if character.isalnum():
                    
                    cleaned_key += character
            propertykeys_cleaned.append(cleaned_key)

        for l in range(len(propertykeys_cleaned)):
            if propertykeys_cleaned[l][0].isnumeric():
                propertykeys_cleaned[l] = propertykeys_cleaned[l][1:-1]

        metadata     = cleaned_emails_list[k]
        content      = cleaned_emails_list[k]['content']
        insert_query = "merge(mail:email:information {content:\"%s\"})\n"%(content)

        for l in range(len(propertykeys)):
            if type(metadata[propertykeys[l]]) !=  'list':
                insert_query = insert_query+ "set mail.%s = \"%s\"\n"%(propertykeys_cleaned[l],metadata[propertykeys[l]])
        print('query built')

        with driver.session() as session:
            session.run(insert_query)
        print('query run, %d mails inserted'%(k))

    return(insert_query)

#%%
emails_list = retrieve_dumped_information(mails_path,'emails_list.json' )

cleaned_emails_list = prepare_strings_for_cypher(emails_list)
cleaned_emails_list = create_emails_urls(cleaned_emails_list,initial_path = 'https://webdisk.ads.mwn.de/Default.aspx?file=Home%2FPA%2Fdata_directory_emps%2F')
insertquery = insert_single_mail(cleaned_emails_list)
