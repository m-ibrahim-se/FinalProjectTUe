import logging
logging.basicConfig(level=logging.INFO)
logging.basicConfig(filename='app.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')


import os
import json
import sys
from conf import *
from crown_jewels import *
import custom_functions
from neo4j import GraphDatabase
import itertools
########################################################################################################################

driver = GraphDatabase.driver(neo4juri, auth=(neo4jusername, neo4jpassword), encrypted=False, database=neo4jdbname)


##########################################################
##########            BDDs and IBDs             ##########
##########################################################

def relate_blocks_with_is_of_type(relations):
    for relation in relations:
        query = """
                MATCH (parent {id:'%s'}) WITH parent MATCH (child{id:'%s'})
                MERGE (child)-[rel:IS_OF_TYPE]->(parent)
                RETURN child, parent
                """ % (relation['superclass_id'], relation['subclass_id'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_blocks_with_is_of_type executed successfully')


def relate_blocks_with_instances(relations):
    for relation in relations:
        query = """
                MATCH (parent {id:'%s'}) WITH parent MATCH (child{id:'%s'})
                MERGE (child)-[rel:IS_INSTANCE_OF]->(parent)
                SET child:INSTANCE
                WITH child, parent
                WHERE child.name IS NULL
                SET child.name = parent.name + '_instance'
                RETURN child, parent
                """ % (relation['type'], relation['id'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_blocks_with_instances executed successfully')


def relate_blocks_with_composition_and_aggregation(is_part_ofs):
    for rel in is_part_ofs:
        query = """
                MATCH (superclass{id:'%s'}), (subclass{id:'%s'})
                MERGE (subclass)-[r:IS_PART_OF]->(superclass)
                SET r.id = '%s'
                SET r.type = '%s'
                RETURN superclass, subclass
        """ % (rel['superclass_id'], rel['subclass_id'], rel['connection_id'], rel['type'])
        logging.debug(query)
        print("Insert query: ",query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_blocks_with_composition_and_aggregation executed successfully')


def relate_ports_with_ports_or_blocks(is_part_ofs):
    for rel in is_part_ofs:
        query = """
                MATCH (superclass{id:'%s'}), (subclass{id:'%s'})
                MERGE (subclass)-[r:IS_PART_OF]->(superclass)
                SET r.type = '%s'
                RETURN superclass, subclass
        """ % (rel['superclass_id'], rel['subclass_id'], rel['type'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_ports_with_blocks executed successfully')


def relate_port_instances_with_block_instances(portInstances):
    for portInstance in portInstances:
        query = """
                MATCH(blockInstance: BLOCK:INSTANCE {id: '%s'}), (portInstance:PORT:INSTANCE {id: '%s'})
                MERGE (portInstance) - [:IS_PART_OF] -> (blockInstance)
                """ % (portInstance['block_instance_id'], portInstance['port_instance_id'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_port_instances_with_block_instances executed successfully')


def relate_port_instances_with_port(portInstances):
    for portInstance in portInstances:
        query = """
                MATCH (port:PORT {id: '%s'}), (portInstance:PORT:INSTANCE {id: '%s'})
                MERGE (portInstance) - [:IS_INSTANCE_OF] -> (port)
                """ % (portInstance['port_id'], portInstance['port_instance_id'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_port_instances_with_port executed successfully')


def relate_nested_port_instances_with_superior_port_instance(portInstances):
    for portInstance in portInstances:
        if portInstance['port_instance_id'].count(' ') == 2:
            firstSpacePosition = portInstance['port_instance_id'].find(' ')
            query = """
                    MATCH (childPort:PORT:INSTANCE {port_instance_id: '%s'}), (parentPort:PORT:INSTANCE {port_instance_id: '%s'})
                    MERGE (childPort) - [:IS_PART_OF] -> (parentPort)
                    """ % (portInstance['port_instance_id'], portInstance['port_instance_id'][firstSpacePosition+1:])
            logging.debug(query)
            with driver.session() as session:
                session.run(query)
    logging.info('relate_nested_port_instances_with_superior_port_instance executed successfully')


def add_names_to_port_instances():
    # add names of the port to port instances and add '_port_instance' to the name
    query = """
            MATCH (portInstance:PORT:INSTANCE)-[:IS_PART_OF]->(blockInstance:BLOCK:INSTANCE), (port:PORT)
            WHERE NOT (portInstance)-[:IS_INSTANCE_OF]->(port)
            SET portInstance.name = blockInstance.name + '_port_instance'
            """
    with driver.session() as session:
        session.run(query)
    # add names of the block instance to port instances (fake ports) and add '_port_instance' to the name
    query = """
            MATCH (i:PORT)-[:IS_INSTANCE_OF]->(p:PORT)
            SET i.name = p.name + '_port_instance'
            """
    with driver.session() as session:
        session.run(query)
    logging.info('add_names_to_port_instances executed successfully')


def create_connector_hypernodes(connectors):
    for connector in connectors:
        query = """
                MERGE (connector:HYPERNODE:SYSML:TEC {id: '%s'})
                """ % (connector['id'])
        try:
            queryAppendageName = """
                                 SET connector.name = '%s'
                                 """ % (connector['name'])
        except:
            queryAppendageName = ''

        try:
            queryAppendageEnd1 = """
                                 SET connector.end1id = '%s'
                                 SET connector.end1role = '%s'
                                 """ % (connector['end1']['id'], connector['end1']['role'])
        except:
            queryAppendageEnd1 = ''

        try:
            end1Appendage = """
                            SET connector.end1partWithPort = '%s'
                            """ % (connector['end1']['partWithPort'])
        except:
            end1Appendage = ''

        try:
            queryAppendageEnd2 = """
                                 SET connector.end2id = '%s'
                                 SET connector.end2role = '%s'
                                 """ % (connector['end2']['id'], connector['end2']['role'])
        except:
            queryAppendageEnd2 = ''

        try:
            end2Appendage = """
                            SET connector.end1partWithPort = '%s'
                            """ % (connector['end1']['partWithPort'])
        except:
            end2Appendage = ''

        returnClause = """
                return connector"""
        query = query + queryAppendageName + queryAppendageEnd1 + end1Appendage + queryAppendageEnd2 + end2Appendage + returnClause
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('create_connector_hypernodes executed successfully')


def relate_connectors(connectorPairs):
    for connectorPair in connectorPairs:
        end1id = connectorPair['end1']
        end2id = connectorPair['end2']
        # relate connectors based on direction in magicdraw
        query = """
                MATCH (c:HYPERNODE), (b1:INSTANCE:PORT), (b2:INSTANCE:PORT)
                WHERE c.id = '%s' AND b1.port_instance_id = '%s' AND b2.port_instance_id = '%s'
                MERGE (b1)-[:FLOWS]->(c)-[:FLOWS]->(b2)
                RETURN b1,b2,c
                """ % (connectorPair['id'], end1id, end2id)
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_connectors executed successfully')






def add_item_flows_to_connectors(itemFlows, connectorPairs, flowWithDirections):
    # itemFlows are already in neo4j, until now they are labelled as blocks
    # to determine which port instance is source and which one is target

    # connect flow items with hypernode (until now, there are only the connectors based on the flow direction from MagicDraw/Cameo
    for flow in flowWithDirections:
        for connectorPair in connectorPairs:
            if flow['connector_id'] == connectorPair['id']:
                # figure out if the flow direction is equal to the connector direction
                try:
                    if flow['source_id'] == connectorPair['end1'] and flow['target_id'] == connectorPair['end2']:
                        # if yes, connect flowitem with hypernode (normal one)
                        query = """
                                MATCH(flowitem:BLOCK {id:'%s'}),(hpn:HYPERNODE {id:'%s'})
                                MERGE(flowitem)-[rel:FLOWS_IN]->(hpn)
                                SET flowitem:FLOWITEM
                                SET rel.id = '%s'
                                """ % (flow['flow_item_id'], flow['connector_id'], flow['information_flow_id'])
                        logging.debug(query)
                        with driver.session() as session:
                                session.run(query)
                        logging.info('add_item_flows_to_connectors executed successfully for %s' % flow)
                    elif flow['source_id'] == connectorPair['end2'] and flow['target_id'] == connectorPair['end1']:
                        # if not, because it's a reverse flow, create new hypernodes according to the opposite direction of the flow and connect flow item with it
                        # create reverse hypernode
                        query1 = """
                                 MERGE (hpn:HYPERNODE {id:'%s'})
                                 """ % (flow['connector_id'] + '_reverse')
                        # connect reverse hypernode with port instances
                        query2 = """
                                 MATCH (hpn:HYPERNODE {id:'%s'}),(src {id: '%s'}),(trgt {id: '%s'})
                                 MERGE (src) -[:FLOWS]-> (hpn) -[:FLOWS]-> (trgt)
                                 """ % (flow['connector_id'] + '_reverse', flow['source_id'], flow['target_id'])
                        # connect flow item with reverse hypernode
                        query3 = """
                                 MATCH(flowitem:BLOCK {id:'%s'}),(hpn:HYPERNODE {id:'%s'})
                                 MERGE(flowitem)-[rel:FLOWS_IN]->(hpn)
                                 SET flowitem:FLOWITEM
                                 SET rel.id = '%s'
                                 """ % (flow['flow_item_id'], flow['connector_id'] + '_reverse', flow['information_flow_id'])
                        logging.debug(query1)
                        logging.debug(query2)
                        logging.debug(query3)
                        with driver.session() as session:
                            session.run(query1)
                            session.run(query2)
                            session.run(query3)
                        logging.info('add_item_flows_to_connectors executed successfully for %s' % flow)
                except:
                    print('flowitem %s failed'%flow['connector_id'])
    logging.info('add_item_flows_to_connectors executed')

def reroute_flows_crossing_block_instance_border():
    # for a port on a block instance border two port instances are generated
    # only one is needed and thus the flows attached to the needless port instance have to be assigned to the remaining one

    query1 = """
             MATCH (p:PORT:INSTANCE)
             WHERE NOT (p)-[:IS_PART_OF]->() 
             MATCH (q:PORT:INSTANCE)
             WHERE p.port_id = q.port_id AND p.block_instance_id = q.block_instance_id
             MATCH (p)-[:FLOWS]->(h1:HYPERNODE) 
             MERGE (q)-[:FLOWS]->(h1)
             """

    query2 = """
             MATCH (p:PORT:INSTANCE)
             WHERE NOT (p)-[:IS_PART_OF]->() 
             MATCH (q:PORT:INSTANCE)
             WHERE p.port_id = q.port_id AND p.block_instance_id = q.block_instance_id
             MATCH (h2:HYPERNODE)-[:FLOWS]->(p)
             MERGE (h2)-[:FLOWS]->(q)
             """

    query3 = """
             MATCH (p:PORT:INSTANCE)
             WHERE NOT (p)-[:IS_PART_OF]->() 
             DETACH DELETE p
             """
    with driver.session() as session:
        session.run(query1)
        session.run(query2)
        session.run(query3)
    logging.info('reroute_flows_crossing_block_instance_border executed successfully')


def relate_all_ports_with_is_of_type():
    # relate instances of ports with their block/type of port and add label :PORT to the type of port blocks
    query = """
            MATCH (port:PORT), (block:BLOCK)
            WHERE port.port_type_id = block.id
            MERGE (port)-[:IS_OF_TYPE]->(block)
            """ # add SET block:PORT when Ports are in model
    logging.debug(query)
    with driver.session() as session:
        session.run(query)
    logging.info('relate_all_ports_with_is_of_type executed successfully')


def add_flowitem_label_to_flowitem_types():
    # add flow item label to blocks which are part of flow items / are type of
    query = """
            MATCH (n:FLOWITEM), (b:BLOCK)
            WHERE (n)<-[:IS_OF_TYPE*]-(b) or (n)<-[:IS_PART_OF*]-(b)
            SET b:FLOWITEM
            """
    logging.debug(query)
    with driver.session() as session:
        session.run(query)
    logging.info('add_flowitem_label_to_flowitem_types executed successfully')


def delete_instance_node_of_flowitem():
    query = """
            MATCH (flowitem:FLOWITEM), (instance:BLOCK:INSTANCE)
            WHERE (instance)-[:IS_INSTANCE_OF]->(flowitem)
            DETACH DELETE instance
            """
    logging.debug(query)
    with driver.session() as session:
        session.run(query)
    logging.info('delete_instance_node_of_flowitem executed successfully')


def add_port_label_to_port_type_blocks():
    query = """
            MATCH (port:PORT:INSTANCE), (type:BLOCK)
            WHERE (port)-[:IS_OF_TYPE*]->(type)
            SET type:PORT
            """
    logging.debug(query)
    with driver.session() as session:
        session.run(query)
    logging.info('add_port_label_to_port_type_blocks executed successfully')

def create_flow_between_ports_and_blocks():
    # this functionality became necessary to run algorithms on the data flow path.
    # otherwise the flow only exists between ports and the :IS_PART_OF relation
    # between port and block needs to be taken into account, which creates a huge mess.
    query = """
            MATCH (b:INSTANCE:BLOCK)<-[:IS_PART_OF]-(p:PORT)-[:FLOWS]->(:HYPERNODE)
            MERGE (p)<-[:FLOWS]-(b)
            """
    logging.debug(query)
    with driver.session() as session:
        session.run(query)

    query = """
            MATCH (b:INSTANCE:BLOCK)<-[:IS_PART_OF]-(p:PORT)<-[:FLOWS]-(:HYPERNODE)
            MERGE (p)-[:FLOWS]->(b)
            """
    logging.debug(query)
    with driver.session() as session:
        session.run(query)
    logging.info('create_flow_between_ports_and_blocks executed successfully')


##########################################################
##########               Activities             ##########
##########################################################

def insert_activities(activities):
    for activity in activities:
        query = """
                MERGE(a:TEC:SYSML:ACT:ACTIVITY {id:'%(id)s'})
                """% {'id':activity['id']}
        try:
            add1 =   """
                     SET a.pins = '%(pins)s'
                     """% { 'pins':activity['pins']}
            query = query + add1
        except:
            pass
        try:
            add = """
                  SET a.name = '%s'
                  """ %(activity['name'])
            query = query + add
        except:
            pass
        with driver.session() as session:
            session.run(query)
        logging.info('activities added')

def relate_activities_hierarchically(activityHierarchy):
    for relation in activityHierarchy:
        query = """
                MATCH(child {id: '%(child)s'}),(parent {id: '%(parent)s'})
                MERGE (child) -[:IS_PART_OF]-> (parent)
                """ % {'child': relation['owned_id'], 'parent': relation['owning_id']}
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_activities_hierarchically executed successfully')

def relate_activity_instances(activityInstances):
    for instance in activityInstances:
        query = """
                MATCH(instance{id:'%s'}), (class{id:'%s'})
                MERGE(instance) -[:IS_EXECUTION_OF]-> (class)
                SET instance.placeholder = TRUE
                """%(instance['instance_activity'],  instance['class_activity'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_activity_instances executed successfully')

def insert_activity_nodes(activityNodes):
    for actNode in activityNodes:
        query = """
                MERGE(a:TEC:SYSML:ACT:ACTNODE{id:'%s'})
                SET a.name = '%s'
                SET a.nodetype = '%s'
                SET a.type = '%s'
                """%(actNode['id'], actNode['name'], actNode['nodetype'],actNode['type'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('insert_activity_nodes executed successfully')

def relate_activity_nodes_with_activities(activityNodes):
    for actnode in activityNodes:
        query = """
                MATCH (a:ACTIVITY{id:'%s'}), (node:ACTNODE {id:'%s'})
                MERGE (node)-[:IS_PART_OF]->(a)
                """%(actnode['parent_activity_id'],actnode['id'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_activity_nodes_with_activities executed successfully')

def add_control_flows(activityControlFlows):
    for cf in activityControlFlows:
        query = """
                MATCH(source {id:'%(src)s'}), (target {id:'%(trgt)s'})
                MERGE (source) -[rel:CONTROL_FLOW]-> (target)
                SET rel.id = '%(cfid)s'
                """%{'src':cf['source_id'], 'trgt':cf['target_id'], 'cfid':cf['id'] }
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
        logging.info('add_control_flows executed successfully')


def add_control_flow_guards(activityControlFlowGuards):
    for guard in activityControlFlowGuards:
        query = """
                MATCH ()-[rel:CONTROL_FLOW {id: '%s'}]- ()
                SET rel.condition = '%s'
                """%(guard['connected_control_flow_id'], guard['text'])

        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('add_control_flow_guards executed successfully')

def create_activity_subinstances():
    query1 = """
            MATCH p = (si:ACTIVITY)-[:IS_EXECUTION_OF]->(act:ACTIVITY)<-[:IS_PART_OF]-(subacts)-[cfs:CONTROL_FLOW*]-(subactnodes)
            WHERE si.placeholder    = TRUE
            MERGE (subactnodes)<-[:IS_EXECUTION_OF]-(subactinstance:TEC:SYSML:ACT)
            SET subactinstance.id   = si.id + subactnodes.id
            SET subactinstance.name = subactnodes.name
            SET subactinstance.type = subactnodes.type
            MERGE (subactinstance) -[:IS_PART_OF]-> (si)
            With *
            UNWIND cfs as cf
            MATCH (a) -[:IS_EXECUTION_OF]-> (prvs) -[cf]-> (nxt) <-[:IS_EXECUTION_OF]- (b)
            MERGE (a) -[rel:CONTROL_FLOW]-> (b)
            SET rel.id = si.id + cf.id
            SET rel.guard = cf.guard
            SET rel.name = cf.name
            WITH *
            MATCH (firstnode) -[inrel:CONTROL_FLOW]-> (si) -[outrel:CONTROL_FLOW]-> (lastnode)
            where not exists { (a) <-[:CONTROL_FLOW]- () } //source node of the instance
            MERGE (firstnode) -[inrelnew:CONTROL_FLOW]-> (a)
            SET inrelnew.id    = si.id + inrel.id
            SET inrelnew.guard = inrel.guard

            """
    query2 = """
            MATCH (prvs) -[inrel:CONTROL_FLOW]-> (si:ACTIVITY {placeholder: TRUE}) -[outrel:CONTROL_FLOW]-> (nxt)
            WITH *
            MATCH (si) -[:IS_EXECUTION_OF]-> (act) <-[:IS_PART_OF]- () -[:CONTROL_FLOW*]- (subnodes) <-[:IS_EXECUTION_OF]- (instancenodes)
            WHERE not exists {(instancenodes)-[:CONTROL_FLOW]->()}//endnode
            MERGE (instancenodes) -[outnew:CONTROL_FLOW]-> (nxt)
            SET outnew.id    = si.id + outrel.id
            SET outnew.guard = outrel.guard
            """
    query3 = """
            MATCH (prvs) -[inrel:CONTROL_FLOW]-> (si:ACTIVITY {placeholder: TRUE}) -[outrel:CONTROL_FLOW]-> (nxt)
            DELETE outrel,inrel
            """

    logging.debug(query1)
    logging.debug(query2)
    logging.debug(query3)
    with driver.session() as session:
        session.run(query1)
        session.run(query2)
        session.run(query3)
    logging.info('create_activity_subinstances executed successfully')


def relate_blocks_with_acts():
    query = """
    MATCH(a:ACT), (b:BLOCK)
    WHERE a.type = b.id
    MERGE(b)-[:IS_USED_IN]->(a)
    """
    logging.debug(query)
    with driver.session() as session:
        session.run(query)
        logging.info('add_uniqueness_constraint_on_magicdraw_id executed successfully')


def relate_object_flows(activityOFlows):
    for oflow in activityOFlows:
        query = """
                MATCH (source:ACT) WHERE source.id = '%(srcid)s' OR source.pins CONTAINS '%(srcid)s'
                WITH source
                MATCH (target:ACT) WHERE target.id = '%(trgtid)s' OR target.pins CONTAINS '%(trgtid)s'
                MERGE (source)-[:OBJECT_FLOW]->(target)
                """%{'srcid':oflow['source'],'trgtid':oflow['target']}
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_object_flows executed successfully')

def add_uniqueness_constraint_on_magicdraw_id():
    query = "CREATE CONSTRAINT magicdraw_id IF NOT EXISTS ON (sysml:SYSML) ASSERT sysml.ID IS UNIQUE"

    logging.debug(query)
    with driver.session() as session:
        session.run(query)
    logging.info('add_uniqueness_constraint_on_magicdraw_id executed successfully')


##########################################################
##########            State Machines            ##########
##########################################################


def relate_stm_with_transitions(transitions):
    for transition in transitions:
        query1 = """
                 MERGE(t:STM:HYPERNODE{id:'%s'})
                 set t:SYSML:TEC
                 """%(transition['id'])
        query2 = """
                MATCH (a{id:'%s'}), (b{id:'%s'}), (t {id:'%s'})
                MERGE (a) -[:TRANSITION]-> (t) -[:TRANSITION]->(b)
                """%(transition['source'],transition['target'],transition['id'])
        try:
            add = "set t.guard = '%s'"%(transition['guard'])
            query2 = query + add
        except:
            pass
        logging.debug(query1)
        logging.debug(query2)
        with driver.session() as session:
            session.run(query1)
            session.run(query2)
    logging.info('relate_stm_with_transitions executed successfully')

def relate_stm_signals(stmSignals):
    for signal in stmSignals:
        query1 = """
                MERGE(signal {id:'%s'})
                set signal:SIGNAL:STM:SYSML:TEC
                set signal.name = '%s'
                """%(signal['id'], signal['name'])
        query2 = ""
        try:
            query2 = """
                    MATCH(signal {id:'%s'}), (transition{id:'%s'})
                    MERGE(signal)-[:TRIGGERS]->(transition)
                    set signal:TRIGGER
                    """%(signal['id'], signal['event'])
        except:
            pass
        logging.debug(query1)
        with driver.session() as session:
            session.run(query1)
            session.run(query2)
    logging.info('relate_stm_signals executed')

def relate_stm_transition_activities(stmTransitionActivities):
    for activity in stmTransitionActivities:
        query = """
                MATCH(activity {id:'%s'}), (transition{id:'%s'})
                MERGE(activity)-[:IS_USED_IN]->(transition)
                """%(activity['activity_id'], activity['transition_id'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)

def relate_stm_hierarchically(states, pseudostates):
    longlist = states + pseudostates
    for state in longlist:
        if 'parentid' in state.keys():
            query = """
                    MATCH (child{id:'%s'}), (parent{id:'%s'})
                    MERGE (child) -[:IS_PART_OF]-> (parent)
                    """%(state['id'], state['parentid'])
            logging.debug(query)
            with driver.session() as session:
                session.run(query)

    query = """
            MATCH(substate)-[:IS_PART_OF]->(state)-[:IS_PART_OF]->(stm), (substate)-[rel:IS_PART_OF]->(stm)
            DELETE rel
            """
    logging.debug(query)
    with driver.session() as session:
        session.run(query)
    logging.info('relate_stm_hierarchically executed successfully')


def reroute_stm_transitions_at_state_entry():
    query1 = """
            MATCH p = (a)-[TRANSITION_FLOW]->(relnode:STM:HYPERNODE)-[tbd:TRANSITION]->(b)<-[:IS_PART_OF]-(ba)
            WHERE ba:STATE or ba:PSEUDOSTATE
            WITH *
            WHERE NOT EXISTS { (ba)<-[:TRANSITION]-()}
            MERGE (relnode)-[:TRANSITION]->(ba)
            set relnode.id = relnode.id + "_redirected_from_"+ b.id
            DELETE tbd
            """
    query2= """
            MATCH p = (b)<-[:IS_PART_OF]-(ba)-[:TRANSITION*2]->(bb)-[:IS_PART_OF]->(b)-[tbd:TRANSITION]->(relnode:STM:HYPERNODE)-[:TRANSITION]->(c)
            WHERE bb:STATE or bb:PSEUDOSTATE
            WITH *
            WHERE NOT EXISTS { (bb)-[:TRANSITION]->()}
            MERGE (bb)-[:TRANSITION]->(relnode)
            set relnode.id = relnode.id + "_redirected_from_"+ b.id
            DELETE tbd
            """
    logging.debug(query1)
    logging.debug(query2)
    with driver.session() as session:
        session.run(query1)
        session.run(query2)
    logging.info('reroute_stm_transitions_at_state_entry executed')


def insert_stm_activities(stmActivities):
    for stmact in stmActivities:
        try:
            query = """
                    MATCH (state {id: '%s'}), (entryref {id:'%s'})
                    MERGE (entry:ACTIVITY:INSTANCE {id:'%s'})
                    MERGE (state)<-[rel:IS_PART_OF]-(entry)
                    MERGE (entry)-[:IS_EXECUTION_OF]->(entryref)
                    set entry.type = 'entry'
                    set entry.name = '%s'
                    """%(stmact['state_id'], stmact['entry_ref'], stmact['entry_id'],stmact['entry_name'])
            logging.debug(query)
            with driver.session() as session:
                 session.run(query)
        except:
            pass

        try:
            query = """
                    MATCH (state {id: '%s'}), (doref {id:'%s'})
                    MERGE (do:ACTIVITY:INSTANCE {id:'%s'})
                    MERGE (state)<-[rel:IS_PART_OF]-(do)
                    MERGE (do)-[:IS_EXECUTION_OF]->(doref)
                    set do.type = 'do'
                    set do.name = '%s'
                    """%(stmact['state_id'], stmact['doActivity_ref'], stmact['doActivity_id'], stmact['doActivity_name'])
            logging.debug(query)
            with driver.session() as session:
                 session.run(query)
        except:
            pass

        try:
            query = """
                    MATCH (state {id: '%s'}), (exitref {id:'%s'})
                    MERGE (exit:ACTIVITY:INSTANCE {id:'%s'})
                    MERGE (state)<-[rel:IS_PART_OF]-(exit)
                    MERGE (exit)-[:IS_EXECUTION_OF]->(exitref)
                    set exit.type = 'exit'
                    set exit.name = '%s'
                    """%(stmact['state_id'], stmact['exit_ref'], stmact['exit_id'],stmact['exit_name'] )
            logging.debug(query)
            with driver.session() as session:
                 session.run(query)
        except:
            pass
    logging.info('insert_stm_activities executed')



def relate_stm_activities():
    query = """
            MATCH (state:STATE)<-[:IS_PART_OF]- (entry:ACTIVITY {type:'entry'}),(state)<-[:IS_PART_OF]- (do:ACTIVITY {type:'do'}),(state)<-[:IS_PART_OF]- (exit:ACTIVITY {type:'exit'})
            MERGE (entry)-[:CONTROL_FLOW]->(do)
            MERGE (do)-[:CONTROL_FLOW]->(exit)
            """
    logging.debug(query)
    with driver.session() as session:
         session.run(query)
    logging.info('relate_stm_activities executed')



##########################################################
##########            Requirements              ##########
##########################################################

# MagicDraw Method Relationships in Neo4J

def relate_requirements_hierarchically(requirementsHierarchy):
    for relation in requirementsHierarchy:
        query = """
                MATCH (subreq:REQUIREMENT {id:'%s'}), (superreq:REQUIREMENT {id:'%s'})
                MERGE (subreq)-[:IS_PART_OF]->(superreq)
                MERGE(subreq)-[rel:IS_TRACED_FROM]->(superreq)
                set rel.sysmltype = 'contained'
                """%(relation['contained'],relation['container'])
        logging.debug(query)
        with driver.session() as session:
             session.run(query)
    logging.info('relate_requirements_hierarchically executed')


def relate_requirements_via_traces(requirementsTraces):
    for relation in requirementsTraces:
        query = """
                MATCH (source{id:'%s'}), (target{id:'%s'})
                MERGE (source)-[rel:IS_TRACED_FROM]->(target)
                set rel.sysmltype = 'trace'"""%(relation['source'], relation['target'])
        logging.debug(query)
        with driver.session() as session:
             session.run(query)
    logging.info('relate_requirements_via_traces executed')

def relate_requirements_via_derives(requirementsDerives):
    for relation in requirementsDerives:
        query = """
                MATCH (source{id:'%s'}), (target{id:'%s'})
                MERGE (source)-[rel:IS_TRACED_FROM]->(target)
                set rel.sysmltype = 'derive'"""%(relation['clientID'], relation['supplierID'])

        logging.debug(query)
        with driver.session() as session:
             session.run(query)
    logging.info('relate_requirements_via_derives executed')

def relate_requirements_via_refines(requirementRefines):
    for relation in requirementRefines:
        query = """
                MATCH (source{id:'%s'}), (target{id:'%s'})
                MERGE (source)<-[rel:IS_TRACED_FROM]-(target)
                set rel.sysmltype = 'refine'"""%(relation['clientID'], relation['supplierID'])

        logging.debug(query)
        with driver.session() as session:
             session.run(query)
    logging.info('relate_requirements_via_refines executed')

def relate_uc_associations(uc_associations):
    for relation in uc_associations:
        query = """
                MATCH (useCase:USECASE{id:'%s'}), (actor:ACTOR{id:'%s'})
                MERGE (actor)<-[rel:IS_TRACED_FROM]-(useCase)
                SET rel.sysmltype = 'association'"""%(relation['useCase'], relation['actor'])
        logging.debug(query)
        with driver.session() as session:
             session.run(query)
    logging.info('relate_uc_associations executed')

def relate_uc_includes(includes_list):
    for include in includes_list:
        query = """
                MATCH (parent:USECASE{id:'%s'}), (child:USECASE {id:'%s'})
                MERGE (child)-[rel:IS_TRACED_FROM]->(parent)
                set rel.sysmltype = 'include'
        """%(include['parent'],include['child'])
        logging.debug(query)
        with driver.session() as session:
             session.run(query)
    logging.info('relate_uc_includes executed')


def relate_uc_extension_points(extensionPoints_list):
    for extP in extensionPoints_list:
        query = """
                MATCH (extP:EXTENSIONPOINT{id:'%s'}), (uc:USECASE {id:'%s'})
                MERGE (extP)-[:IS_PART_OF]->(uc)
        """%(extP['id'], extP['useCase'])
        logging.debug(query)
        with driver.session() as session:
             session.run(query)
    logging.info('relate_uc_extension_points executed')


def relate_uc_extends(extensions_list):
    for ext in extensions_list:
        query = """
                MATCH (parent:USECASE{id:'%s'}), (child:USECASE {id:'%s'}), (extP:EXTENSIONPOINT {id:'%s'})
                MERGE (extP)<-[:REQUIRES]-(child)-[rel:IS_TRACED_FROM]->(parent)
                set rel.sysmltype = 'extend'

        """%(ext['parentUseCase'], ext['useCase'], ext['extensionPoint'])
        logging.debug(query)
        with driver.session() as session:
             session.run(query)
    logging.info('relate_uc_extends executed')


def relate_allocations(allocateRelations):
    for allocate in allocateRelations:
        query="""
              MATCH (supplier{id:'%s'}), (client{id:'%s'})
              MERGE (supplier)<-[rel:IS_TRACED_FROM]-(client)
              set rel.sysmltype = 'allocate'
              """%(allocate['supplier'],allocate['client'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_allocations executed')


def relate_verify_relations(verifyRelations):
    for verify in verifyRelations:
        query="""
              MATCH (requirement:REQUIREMENT {id:'%s'}), (testcase {id:'%s'})
              MERGE(testcase)-[:VERIFIES]->(requirement)
              """%(verify['requirement'], verify['testcase'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_verify_relations executed')


def relate_satisfy_relations(satisfyRelations):
    for satisfy in satisfyRelations:
        query = """
                MATCH (requirement:REQUIREMENT{id:'%s'}), (block{id:'%s'})
                MERGE(requirement)<-[:SATISFIES]-(block)
                """%(satisfy['requirement'],satisfy['block'])
        logging.debug(query)
        with driver.session() as session:
            session.run(query)
    logging.info('relate_satisfy_relations executed')


def relate_usecases_with_generalizations(generalizations):
    relate_blocks_with_is_of_type(generalizations)
    query1 = """
    MATCH (u:USECASE)-[:IS_OF_TYPE]->(u2:USECASE)
    MERGE (u)-[rel:IS_TRACED_FROM]->(u2)
    set rel.sysmltype  = 'generalization'
    """
    query2 = """
    MATCH(a:ACTOR)-[:IS_OF_TYPE]->(a2:ACTOR)
    MERGE(a)-[rel:IS_TRACED_FROM]->(a2)
    set rel.sysmltype = 'generalization'
    """
    with driver.session() as session:
        session.run(query1)
        session.run(query2)
    logging.info('relate_usecases_with_generalizations executed')




##########################################################
##########            Main Function             ##########
##########################################################

def main_function():

    # bdd
    # blocks                   = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_blocks.json")
    # isPartOfsBlocks          = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_is_part_ofs_blocks.json")
    # generalizations          = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_generalizations.json")
    #
    # # ibd
    # blockInstances           = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_block_instances.json")
    # fullPorts                = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_full_ports.json")
    # proxyPorts               = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_proxy_ports.json")
    # ports                    = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_ports.json")
    # flowPorts                = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_flow_ports.json")
    # isPartOfsPorts           = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_is_part_ofs_ports.json")
    # connectors               = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_connectors.json")
    # nestedPorts              = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_nested_ports.json")
    # portInstances            = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_port_instances.json")
    # connectorPairs           = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_connector_pairs.json")
    # itemFlows                = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_item_flows.json")
    # itemFlowsWithDirections  = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_item_flows_with_directions.json")

    # # activities
    # activities               = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_activities.json")
    # activityInstances        = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename='magicdraw_activity_instances.json')
    # activityControlFlows     = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_activity_control_flows.json")
    # activityNodes            = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_activity_nodes.json")
    # activityHierarchy        = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_hierarchic_activity_relations.json")
    # activityControlFlowGuards= custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_activity_control_flow_guards.json")
    # activityOFlows           = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_activity_o_flows.json")
    #
    # # state machines
    states                   = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_stm_states.json")
    pseudostates             = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_stm_pseudostates.json")
    transitions              = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_stm_transitions.json")
    stmTransitionActivities  = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_stm_transition_activities.json")
    stmActivities            = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_state_activities.json")
    stmSignals               = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_stm_signals.json")
    #
    # # Requirements
    # requirements             = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_requirements.json")
    # requirementsHierarchy    = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_requirements_hierarchy.json")
    # requirementsTraces       = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_requirements_traces.json")
    # requirementsDerives      = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_requirements_derives.json")
    # requirementsRefines      = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_requirements_refines.json")
    # useCases                 = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename='magicdraw_use_cases.json')
    # actors                   = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_actors.json")
    # uc_associations          = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_usecase_associations.json")
    # includes_list            = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_usecase_includes.json")
    # extensionPoints_list     = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_usecase_extension_points.json")
    # extensions_list          = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_usecase_extensions.json")
    # allocateRelations        = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_allocate_relations.json")
    # verifyRelations          = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_verify_relations.json")
    # testCases                = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_test_cases.json")
    # satisfyRelations         = custom_functions.retrieve_dumped_information(path=magicdraw_path, filename="magicdraw_satisfy_relations.json")


    #bdd
    # logging.info('\n')
    # logging.info('add blocks to neo4j')
    # custom_functions.universal_insert(dataset=blocks, labels=':BLOCK:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('relate_blocks_with_composition_and_aggregation')
    # relate_blocks_with_composition_and_aggregation(isPartOfsBlocks)
    #
    # logging.info('\n')
    # logging.info('relate_blocks_with_is_of_type')
    # relate_blocks_with_is_of_type(generalizations)


    # ibd
    # logging.info('\n')
    # logging.info('add block instances to neo4j')
    # custom_functions.universal_insert(dataset=blockInstances, labels=':BLOCK:INSTANCE:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('relate_blocks_with_instances')
    # relate_blocks_with_instances(blockInstances)
    #
    # logging.info('\n')
    # logging.info('add fullports to neo4j')
    # custom_functions.universal_insert(dataset=fullPorts, labels=':PORT:FULLPORT:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('add proxy ports to neo4j')
    # custom_functions.universal_insert(dataset=proxyPorts, labels=':PORT:PROXY:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('add ports to neo4j')
    # custom_functions.universal_insert(dataset=ports, labels=':PORT:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('add flow ports to neo4j')
    # custom_functions.universal_insert(dataset=flowPorts, labels=':PORT:FLOWPORT:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('relate_ports_with_ports_or_blocks (blocks)')
    # relate_ports_with_ports_or_blocks(isPartOfsPorts)
    #
    # logging.info('\n')
    # logging.info('relate_ports_with_ports_or_blocks (ports - nested ports)')
    # relate_ports_with_ports_or_blocks(nestedPorts)
    #
    #
    # logging.info('\n')
    # logging.info('add port instances to neo4j')
    # custom_functions.universal_insert(dataset=portInstances, labels=':PORT:INSTANCE:SYSML:TEC', id_key='port_instance_id')
    #
    # logging.info('\n')
    # logging.info('relate port instances to their block instances in neo4j')
    # relate_port_instances_with_block_instances(portInstances)
    #
    # logging.info('\n')
    # logging.info('relate port instances to their port in neo4j')
    # relate_port_instances_with_port(portInstances)
    #
    # logging.info('\n')
    # logging.info('relate nested port instances to the port instance of the superior port in neo4j')
    # relate_nested_port_instances_with_superior_port_instance(portInstances)
    #
    # logging.info('\n')
    # logging.info('relate_all_ports_with_is_of_type')
    # relate_all_ports_with_is_of_type()
    #
    # logging.info('\n')
    # logging.info('add_names_to_port_instances')
    # add_names_to_port_instances()
    #
    # logging.info('\n')
    # logging.info('create_connector_hypernodes')
    # create_connector_hypernodes(connectors)
    #
    # logging.info('\n')
    # logging.info('relate_connectors')
    # relate_connectors(connectorPairs)
    #
    # logging.info('\n')
    # logging.info('add_item_flows_to_connectors')
    # add_item_flows_to_connectors(itemFlows, connectorPairs, itemFlowsWithDirections)
    #
    # logging.info('\n')
    # logging.info('reroute_flows_crossing_block_instance_border')
    # reroute_flows_crossing_block_instance_border()
    #
    # logging.info('\n')
    # logging.info('add_flowitem_label_to_flowitem_types')
    # add_flowitem_label_to_flowitem_types()
    #
    # logging.info('\n')
    # logging.info('delete_instance_node_of_flowitem')
    # delete_instance_node_of_flowitem()
    #
    # logging.info('\n')
    # logging.info('add_port_label_to_port_type_blocks')
    # add_port_label_to_port_type_blocks()
    #
    # logging.info('\n')
    # logging.info('create_flow_between_ports_and_blocks')
    # create_flow_between_ports_and_blocks()

    # #act
    # logging.info('\n')
    # logging.info('insert_activity_nodes(activities)')
    # insert_activities(activities)
    #
    # logging.info('\n')
    # logging.info('relate_activities_hierarchically(activityHierarchy)')
    # relate_activities_hierarchically(activityHierarchy)
    #
    # logging.info('\n')
    # logging.info('relate_activity_instances(activityInstances)')
    # relate_activity_instances(activityInstances)
    #
    # logging.info('\n')
    # logging.info('insert_activity_nodes(activityNodes)')
    # insert_activity_nodes(activityNodes)
    #
    # logging.info('\n')
    # logging.info('relate_activity_nodes_with_activities(activityNodes)')
    # relate_activity_nodes_with_activities(activityNodes)
    # logging.info('\n')
    # logging.info('add_control_flows(activityControlFlows)')
    # add_control_flows(activityControlFlows)
    #
    # logging.info('\n')
    # logging.info('add_control_flow_guards(activityControlFlowGuards)')
    # add_control_flow_guards(activityControlFlowGuards)
    #
    # logging.info('\n')
    # logging.info('add_control_flow_guardscreate_activity_subinstances')
    # create_activity_subinstances()
    #
    # logging.info('\n')
    # logging.info('relate_blocks_with_acts()')
    # relate_blocks_with_acts()
    #
    # logging.info('\n')
    # logging.info('relate_object_flows()')
    # relate_object_flows(activityOFlows)
    #
    # # stm
    #
    logging.info('\n')
    logging.info('insert states')
    custom_functions.universal_insert(dataset = states, labels=':STATE:SYSML:TEC', id_key='id')

    logging.info('\n')
    logging.info('insert pseudostates')
    custom_functions.universal_insert(dataset = pseudostates, labels=':PSEUDOSTATE:SYSML:TEC', id_key='id')

    logging.info('\n')
    logging.info('insert stm transitions')
    relate_stm_with_transitions(transitions)

    logging.info('\n')
    logging.info('relate stm transitions with triggers')
    relate_stm_signals(stmSignals)

    logging.info('\n')
    logging.info('relate stm transitino with transition activities')
    relate_stm_transition_activities(stmTransitionActivities)

    logging.info('\n')
    logging.info('insert stm hierarchy')
    relate_stm_hierarchically(states,pseudostates)

    logging.info('\n')
    logging.info('reroute_stm_transitions_at_state_entry')
    reroute_stm_transitions_at_state_entry()

    logging.info('\n')
    logging.info('insert_stm_activities')
    insert_stm_activities(stmActivities)

    logging.info('\n')
    logging.info('relate_stm_activities')
    relate_stm_activities()

    # logging.info('\n')
    # logging.info('create_flow_between_ports_and_blocks')
    # create_flow_between_ports_and_blocks()




    # # Requirements
    #
    # logging.info('\n')
    # logging.info('add Requirements to neo4j')
    # custom_functions.universal_insert(dataset=requirements, labels=':REQUIREMENT:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('add Use Cases to neo4j')
    # custom_functions.universal_insert(dataset=useCases, labels=':USECASE:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('add Use Case Extension Points to neo4j')
    # custom_functions.universal_insert(dataset=extensionPoints_list, labels=':EXTENSIONPOINT:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('add Actors to neo4j')
    # custom_functions.universal_insert(dataset=actors, labels=':ACTOR:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('add Testcases to neo4j')
    # custom_functions.universal_insert(dataset=testCases, labels=':ACTIVITY:SYSML:TEC', id_key='id')
    #
    # logging.info('\n')
    # logging.info('create requirements hierarchy')
    # relate_requirements_hierarchically(requirementsHierarchy)
    #
    # logging.info('\n')
    # logging.info('create requirement traces')
    # relate_requirements_via_traces(requirementsTraces)
    #
    # logging.info('\n')
    # logging.info('create requirement derives')
    # relate_requirements_via_derives(requirementsDerives)
    #
    # logging.info('\n')
    # logging.info('create requirement refines')
    # relate_requirements_via_refines(requirementsRefines)
    #
    # logging.info('\n')
    # logging.info('create uc includes')
    # relate_uc_includes(includes_list)
    #
    # logging.info('\n')
    # logging.info('create uc extP relations')
    # relate_uc_extension_points(extensionPoints_list)
    #
    # logging.info('\n')
    # logging.info('create uc extensions')
    # relate_uc_extends(extensions_list)
    #
    # logging.info('\n')
    # logging.info('create uc associations')
    # relate_uc_associations(uc_associations)
    #
    # logging.info('\n')
    # logging.info('create verify relations')
    # relate_verify_relations(verifyRelations)
    #
    # logging.info('\n')
    # logging.info('create allocate relations')
    # relate_allocations(allocateRelations)
    #
    # logging.info('\n')
    # logging.info('create satisfy relations')
    # relate_satisfy_relations(satisfyRelations)
    #
    # logging.info('\n')
    # logging.info('create use case generalizations and generalizations traced from relations')
    # relate_usecases_with_generalizations(generalizations)
    #
    # # logging.info('\n')
    # # logging.info('add Requirements-Activities Relations to neo4j')
    # # relate_requirements_with_activities (req_abstractions)




#%%
if __name__ == '__main__':
    main_function()
