import logging
logging.basicConfig(level=logging.INFO)
logging.basicConfig(filename='app.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')


import retrieve_SysML_model
import insert_SysML_in_neo4j



retrieve_SysML_model.main_function()
insert_SysML_in_neo4j.main_function()
