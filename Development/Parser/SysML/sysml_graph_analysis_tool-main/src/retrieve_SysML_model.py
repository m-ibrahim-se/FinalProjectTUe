import logging
logging.basicConfig(level=logging.INFO)
logging.basicConfig(filename='app.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')

import sys  # sys.exit("Stop")
import json
import os
import re
import custom_functions
from conf import *
from os.path import join
import xml.etree.ElementTree as ET

# definitions of xmi, uml and profileElement for parsing .mdxml files
def get_prefixes(pathToFile):
    global xmi, sysml, MD_Customization_for_SysML__additional_stereotypes
    with open(pathToFile, "r") as myfile:
        data = myfile.read()

        # create xmi key string
        xmiRaw = re.findall(r"xmlns:xmi='.+?'", data)
        xmi = "{" + xmiRaw[0][11:-1] + "}"

        # create sysml key string
        # sysmlRaw = re.findall(r"xmlns:sysml='.+?'", data)
        # sysml = "{" + sysmlRaw[0][13:-1] + "}"
        # logging.debug('prefixes defined')
        # # create MD_Customization_for_SysML__additional_stereotypes key string
        # MD_Customization_for_SysML__additional_stereotypesRaw = re.findall(r"xmlns:MD_Customization_for_SysML__additional_stereotypes='.+?'", data)
        # MD_Customization_for_SysML__additional_stereotypes = "{" + MD_Customization_for_SysML__additional_stereotypesRaw[0][58:-1] + "}"


##########################################################
##########            BDDs and IBDs             ##########
##########################################################

def get_sysml_blocks_from_mdxml(root):
    sysmlBlocks = list()
    for element in root.iter(sysml + 'Block'):
        block = {
            "sysml_id": element.attrib[xmi + "id"],
            "base_Class": element.attrib["base_Class"]
        }
        sysmlBlocks.append(block)
    logging.info('Blocks identified via get_sysml_blocks_from_mdxml')
    return sysmlBlocks


def get_blocks_from_mdxml(root, sysmlBlocks):
    blocks = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi + "type") == "uml:Class":
            try:
                classElement = {
                    "id": element.attrib[xmi + "id"],
                    "name": element.attrib["name"],
                }
                # checks if the id of the class element is identical to the base Class number of a sysml:Block element from the XML
                for block in sysmlBlocks:
                    if block['base_Class'] == classElement['id']:
                        # block.update(classElement)
                        blocks.append(classElement)
                logging.debug('Block added via get_blocks_from_mdxml')
            except:
                logging.debug('classElement which has no id or name')
    for element in root.iter('nestedClassifier'):
        if element.get(xmi + "type") == "uml:Class":
            classElement = {
                "id": element.attrib[xmi + "id"],
                "name": element.attrib["name"],
            }
            # checks if the id of the class element is identical to the base Class number of a sysml:Block element from the XML
            for block in sysmlBlocks:
                if block['base_Class'] == classElement['id']:
                    # block.update(classElement)
                    blocks.append(classElement)
            logging.debug('Block added via get_blocks_from_mdxml')
    logging.info('get_blocks_from_mdxml executed successfully')
    return blocks


def get_block_instances_from_mdxml(root, sysmlProperties):
    instances = list()
    for sysmlProperty in sysmlProperties:
        for attribute in root.iter("ownedAttribute"):
            if attribute.get(xmi + "type") == "uml:Property" and attribute.get("type") == sysmlProperty['base_Class']:
                instance = dict()
                instance["id"] = attribute.attrib[xmi + "id"]
                try:
                    instance["name"] = attribute.attrib["name"]
                except:
                    logging.debug('property with id %s has no name' % (instance['id']))
                try:
                    instance["type"] = attribute.attrib["type"]
                except:
                    logging.debug('property with id %s has no type-id' % (instance['id']))
                instances.append(instance)
    logging.info('block instances drawn from mdxml via get_block_instances_from_mdxml')
    return instances


def get_generalization_from_mdxml(root):
    generalizations = list()
    for element in root.iter('packagedElement'):
        for child in element.iter('generalization'):
            if child.get(xmi + 'type') == 'uml:Generalization':
                    generalization=dict()
                    generalization["subclass_id"] = element.get(xmi + "id")
                    generalization["superclass_id"] = child.get('general')
                    generalizations.append(generalization)
    logging.info('generalizations drawn from mdxml via get_generalization_from_mdxml')
    return generalizations


def get_block_instance_relations_from_mdxml(root, sysmlBlocks):
    isInstanceOfs = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi + "type") == "uml:Class":
            for child in element:
                if child.tag == "ownedAttribute" and child.attrib[xmi + 'type'] == 'uml:Property':
                    for sysmlBlock in sysmlBlocks:
                        try:
                            if sysmlBlock['base_Class'] == child.attrib['type']:
                                isInstanceOf = dict()
                                isInstanceOf["superclass_id"] = child.attrib['type']
                                isInstanceOf["subclass_id"] = child.attrib[xmi + "id"]
                                isInstanceOfs.append(isInstanceOf)
                        except:
                            logging.debug('child (uml:Property) has no type')
    logging.info('got the information which instances belong to which block via get_block_instance_relations_from_mdxml')
    return isInstanceOfs


def get_is_part_of_relations_for_blocks_from_mdxml(root):
    # gets the relation type "composite" or "aggregation" ("shared") which is used between blocks in bdds
    is_part_ofs = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi + "type") == "uml:Class":
            for child in element:
                if child.tag == "ownedAttribute" and child.attrib[xmi + 'type'] == 'uml:Property' and 'aggregation' in child.attrib:
                    try:
                        is_part_of = dict()
                        is_part_of['type'] = child.attrib['aggregation']  # composite or aggregation (shared)
                        is_part_of["superclass_id"] = element.attrib[xmi + "id"]
                        is_part_of["subclass_id"] = child.attrib['type']
                        is_part_of['connection_id'] = child.attrib['association']
                        is_part_ofs.append(is_part_of)
                    except:
                        pass
    logging.info('is_part_ofs relations for blocks drawn from mdxml via get_is_part_of_relations_for_blocks_from_mdxml')
    return is_part_ofs


def get_sysml_ports_from_mdxml(root, portType):
    # only Flow Port, Full Port and Proxy Port are SysML Ports, "uml ports/normal ports" are of type port
    sysmlPorts = list()
    for port in root.iter(sysml + portType):
        sysmlPort = {
            "sysml_id": port.attrib[xmi + "id"],
            "base_Port": port.attrib["base_Port"]
        }
        sysmlPorts.append(sysmlPort)
    logging.debug('Port types identified via get_sysml_ports_from_mdxml')
    return sysmlPorts


def get_is_part_of_relations_for_ports_from_mdxml(root, nestedPorts):
    # get the information which port belongs to which block, but if a port is already part of a block, this information is not gathered (nested port case)
    is_part_ofs = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi + "type") == "uml:Class":
            for child in element:
                if child.tag == "ownedAttribute" and child.attrib[xmi+'type'] == 'uml:Port' and 'aggregation' in child.attrib:
                    # check if port is a nested port, if port is part of a port -> don't add is_part_of relation between this port and a port
                    isNestedPort = False
                    for nestedPort in nestedPorts:
                        if child.attrib[xmi + 'id'] == nestedPort['subclass_id']:
                            isNestedPort = True
                    if isNestedPort is False:
                        try:
                            is_part_of = dict()
                            is_part_of['type'] = child.attrib['aggregation'] # composite or aggregation
                            is_part_of["superclass_id"] = element.attrib[xmi + "id"]
                            is_part_of["subclass_id"] = child.attrib[xmi + 'id']
                            is_part_ofs.append(is_part_of)
                        except:
                            pass
    logging.info('is_part_ofs relations for ports drawn from mdxml via get_is_part_of_relations_for_ports_from_mdxml')
    return is_part_ofs


def get_ports_from_mdxml(root, sysmlFullPorts, sysmlProxyPorts, sysmlFlowPorts):
    # gets all ports and separates them into full and proxy. Compare port with list of full and proxy ports
    fullPorts = list()
    proxyPorts = list()
    ports = list()
    flowPorts = list()
    portLocations = ['packagedElement', 'nestedClassifier']
    for location in range(len(portLocations)):
        for element in root.iter(portLocations[location]):
            if element.get(xmi + "type") == "uml:Class":
                for attribute in element:
                    if attribute.tag == "ownedAttribute" and attribute.get(xmi + "type") == "uml:Port":
                        port = {
                            #"component_name": element.attrib["name"],
                            #"component_id": element.attrib[xmi + "id"],
                            "name": attribute.attrib["name"],
                            "id": attribute.attrib[xmi + "id"]
                        }
                        if 'isConjugated' in attribute.keys() and attribute.attrib['isConjugated'] == 'true':
                            port['isConjugated'] = 'true'
                        else:
                            port['isConjugated'] = 'false'

                        if "type" in attribute.keys():
                            port["port_type_id"] = attribute.attrib["type"]
                        # there is no sysmlPorts for normal (uml?) ports in xml
                        umlPort = True
                        for sysmlFullPort in sysmlFullPorts:
                            if port["id"] in sysmlFullPort["base_Port"]:
                                fullPorts.append(port)
                                umlPort = False

                        for sysmlProxyPort in sysmlProxyPorts:
                            if port["id"] in sysmlProxyPort["base_Port"]:
                                proxyPorts.append(port)
                                umlPort = False

                        for sysmlFlowPort in sysmlFlowPorts:
                            if port["id"] in sysmlFlowPort["base_Port"]:
                                flowPorts.append(port)
                                umlPort = False

                        if umlPort:
                            ports.append(port)
    logging.info('ports succesfully drawn from mdxml via get_ports_from_mdxml')
    return fullPorts, proxyPorts, ports, flowPorts


def get_sysml_nested_connector_ends_from_mdxml(root):
    nestedConnectorEnds = list()
    for element in root.iter(sysml + 'NestedConnectorEnd'):
        try:
            nestedConnectorEnd = dict()
            nestedConnectorEnd['id'] = element.attrib[xmi + 'id']
            nestedConnectorEnd['base_ConnectorEnd'] = element.attrib['base_ConnectorEnd']
            nestedConnectorEnd['propertyPath'] = element.attrib['propertyPath']
            # for connector ends with nested ports the propertyPath consists of two ids. The next 3 lines separate the two ids.
            propertyPathIds = nestedConnectorEnd['propertyPath'].split(' ')
            if len(propertyPathIds) > 1:
                nestedConnectorEnd['propertyPathPart1'] = propertyPathIds[0]
                nestedConnectorEnd['propertyPathPart2'] = propertyPathIds[1]
                logging.info('propertyPath consists of two element ids')
            else:
                logging.info('propertyPath consists only of one element id')
            nestedConnectorEnds.append(nestedConnectorEnd)
        except:
            logging.warning('sysml nestedConnectorElement %s could not entirely be read out from mdxml. Some information be missing in the sysml model' % element.attrib[xmi + 'id'])
    logging.info('nested ports succesfully drawn from mdxml via get_sysml_nested_connector_ends_from_mdxml')
    return nestedConnectorEnds


def get_connector_end_data(connectorend):
    end = dict()
    end["id"] = connectorend.attrib[xmi + "id"]
    if "partWithPort" in connectorend.keys():
        end["partWithPort"] = connectorend.attrib["partWithPort"]
    if "role" in connectorend.keys():
        end["role"] = connectorend.attrib["role"]
    logging.debug('read out the connector end data of the provided connector via get_connector_end_data')
    return end


# this function gets all connectors from the sysml file, including their names and the blocks they are linked to.
def get_connectors_from_mdxml(root):
    connectors = list()
    for element in root.iter('ownedConnector'):  # connectors are stored in the 'ownedConnector' xml element
        connector = dict()
        connector['id'] = element.attrib[xmi + 'id']
        try:
            connector['name'] = element.attrib['name']
        except:
            pass
        endNumber = 1
        for child in element:
            if child.tag == "end" and child.get(xmi + "type") == "uml:ConnectorEnd":
                connectorend = child
                connector["end" + str(endNumber)] = get_connector_end_data(connectorend)
                endNumber += 1
        connectors.append(connector)
    logging.info('connectors drawn from mdxml via get_connectors_from_mdxml')
    return connectors


def get_nested_port_couples_from_mdxml(connectors, fullPorts, proxyPorts, ports, flowPorts):
    ports = fullPorts + proxyPorts + ports + flowPorts
    nestedPorts = list()
    for connector in connectors:
        for end in ['end1', 'end2']:
            # check for each connector end if the keys 'partWithPort' and 'role' exist
            if 'partWithPort' in connector[end] and 'role' in connector[end]:
                # check if both values are port IDs
                nestedPort = dict()
                for port in ports:
                    if connector[end]['partWithPort'] == port['id']:
                        nestedPort['superclass_id'] = connector[end]['partWithPort']
                    if connector[end]['role'] == port['id']:
                        nestedPort['subclass_id'] = connector[end]['role']
                nestedPort['type'] = 'customized_relation'
                if 'superclass_id' in nestedPort and 'subclass_id' in nestedPort and nestedPort not in nestedPorts:
                    nestedPorts.append(nestedPort)
    logging.info('nested ports drawn from mdxml via get_nested_port_couples_from_mdxml')
    return nestedPorts


def get_port_instances_and_pairing(connectors, partOfsPorts, nestedPorts, blockInstances, nestedConnectorEnds):
    portInstances = list()
    connectorPairs = list()
    for connector in connectors:
        connectorPair = dict()
        connectorPair['id'] = connector['id']
        for end in ["end1", "end2"]:
            isNestedPort = False
            portInstance = dict()
            if "partWithPort" in connector[end]:
                # if true, partWithPort can refer to a block instance or to a port (only nested port), in both cases the system works,
                # but the names of the dictionary keys are misleading
                for nestedPort in nestedPorts:
                    if connector[end]["role"] == nestedPort["subclass_id"]:
                        isNestedPort = True

                if isNestedPort is True:
                    # port is nested port
                    # TODO: change portInstance key "block_instance_id" into "..." since the port ist not necessarily attached to a block instance (could be also a port)
                    for nestedPort in nestedPorts:
                        if connector[end]["role"] == nestedPort["subclass_id"]:
                            portInstance['block_instance_id'] = nestedPort["superclass_id"]
                            portInstance["port_id"] = connector[end]["role"]
                            nestedPort = False
                            for nestedConnectorEnd in nestedConnectorEnds:
                                if connector[end]['id'] == nestedConnectorEnd['base_ConnectorEnd'] and 'propertyPathPart1' in nestedConnectorEnd.keys():
                                    # nestedConnectorEnd is not just a port that is at the end of a connector with a nestedPort at on of its ends, but is not really nested
                                    nestedPort = True
                                    portInstance['port_instance_id'] = portInstance['port_id'] + " " + portInstance['block_instance_id'] + " " + nestedConnectorEnd['propertyPathPart1']

                            if nestedPort is False:
                                portInstance['port_instance_id'] = portInstance['port_id'] + " " + portInstance['block_instance_id']

                            if portInstance not in portInstances:
                                portInstances.append(portInstance)
                else:
                    portInstance['block_instance_id'] = connector[end]['partWithPort']
                    portInstance['port_id'] = connector[end]['role']
                    # the port instance id is the combination of the uml port id and the uml instance id
                    portInstance['port_instance_id'] = portInstance['port_id'] + " " + portInstance['block_instance_id']
                    if portInstance not in portInstances:
                        portInstances.append(portInstance)
            else:
                # if else, this connector can be a port or an instance of a block
                # if it's a port, it's not a nested one but it could be classified as a nested port and thus be in the nestedPorts list
                # this is the case when one end of the connector is a nested port -> both ends are classified as nestedConnectorEnds

                # if the role belongs to a port, port id = role and we have to find the block this port belongs to and get the instance of this block
                # port is not a nested port
                for partOfsPort in partOfsPorts:
                    if connector[end]["role"] == partOfsPort["subclass_id"]:
                        # it's a port -> we need the block this port belongs to
                        # If the partWithPort property is not provided the port is on a diagram border, where the diagram border represents an instance of a block
                        # Then there SHOULD be always also a second diagram where the port is on the instance border but not on the diagram border
                        # -> As a consequence it is possible to distinguish between the instances the ports are part of
                        # The code generates an instance for each port - instance border couple and since MERGE is used a multitude of these couples is no problem
                        # Whenever the port is on an diagram border a port - all instances of the block is generated -> which should be quite correct
                        for blockInstance in blockInstances:
                            if partOfsPort["superclass_id"] == blockInstance["type"]:
                                # TODO: is the subclass automatically the correct instance? What if a block has two instances?
                                portInstance['block_instance_id'] = blockInstance["id"]
                                portInstance["port_id"] = connector[end]["role"]
                                portInstance['port_instance_id'] = portInstance['port_id'] + " " + portInstance['block_instance_id']
                                if portInstance not in portInstances:
                                    portInstances.append(portInstance)
                    #else:
                # check if the connector[end]['role'] is an instance and if so, create an artificial port instance for the instance
                for blockInstance in blockInstances:
                    # TODO: is the subclass automatically the correct instance? What if a block has two instances?
                    if connector[end]['role'] == blockInstance["id"]:
                        portInstance['block_instance_id'] = connector[end]['role']
                        # in this case of the connector end being an instance of a block the port instance (artificial) id is the connector end id
                        portInstance["port_id"] = connector[end]['id']
                        portInstance['port_instance_id'] = portInstance['port_id'] + " " + portInstance['block_instance_id']
                        if portInstance not in portInstances:
                            portInstances.append(portInstance)
            connectorPair[end] = portInstance['port_instance_id']
        if connectorPair not in connectorPairs:
            connectorPairs.append(connectorPair)
    logging.debug('port instances derived from connector ends and grouped according to their connection via get_port_instances_and_pairing')

    # create port instances for ports that nests other ports, but are not at the end of a connector
    for nestedConnectorEnd in nestedConnectorEnds:
        if 'propertyPathPart1' in nestedConnectorEnd and 'propertyPathPart2' in nestedConnectorEnd:
            portInstance = dict()
            portInstance['block_instance_id'] = nestedConnectorEnd['propertyPathPart1']
            portInstance['port_id'] = nestedConnectorEnd['propertyPathPart2']
            portInstance['port_instance_id'] = portInstance['port_id'] + " " + portInstance['block_instance_id']
            if portInstance not in portInstances:
                portInstances.append(portInstance)
    return portInstances, connectorPairs


def get_sysml_itemflows_from_mdxml(root):
    itemFlows = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi+'type') == 'uml:InformationFlow':
            # get the number of realizing connectors in an informationFlow
            # there is a seperate connector for each flow item in a block instance, so that the same flow item in e.g.
            # the instances a:A and b:A are on the same connector, but a different flow item alsways has its own connector
            # for each realizing connector in an informationFlow we duplicate the informatinFlow and only take one realizing connector
            for realizingConnector in element.findall('realizingConnector'):
                itemFlow = dict()
                itemFlow['information_flow_id'] = element.attrib[xmi + 'id']
                for child in element:
                    if child.tag == 'informationSource':
                        itemFlow['information_source'] = child.attrib[xmi + 'idref']
                    if child.tag == 'informationTarget':
                        itemFlow['information_target'] = child.attrib[xmi + 'idref']
                    if child.tag == 'realizingConnector':
                        itemFlow['realizing_connector_id'] = realizingConnector.attrib[xmi + 'idref']
                    if child.tag == 'conveyed':
                        itemFlow['flow_item_id'] = child.attrib[xmi + 'idref']
                itemFlows.append(itemFlow)
    logging.info('item flows drawn from mdxml via get_sysml_itemflows_from_mdxml')
    return itemFlows


def get_type_of_information_type(element, blocks, blockInstances, portInstances):
    # check if element is of type block, block instance or port
    # block or block instance
    for block in blocks:
        if element == block["id"]:
            return 'block'

    for blockInstance in blockInstances:
        if element == blockInstance["id"]:
            return 'instance'

    # check if element is of type port
    for port in portInstances:
        if element == port['port_id']:
            return 'port'

def determine_itemflow_direction(itemFlows, connectorPairs, blocks, blockInstances, portInstances, connectors, nestedConnectorEnds, fullPorts, proxyPorts, ports, flowPorts, isPartOfsPorts):
    k=0
    l=0
    m=0
    n=len(itemFlows)
    o=0
    # a connector pair contains the connector id and the ids of the port instances at the connector ends, but not which one is the source and which one is target
    # an itemFlow contains the source and target information, but in the form of blocks, block instances, port types (blocks) or ports
    # -> this function combines the port instance and source/target informatin to determine which port instance is source and which is target
    flowsWithDirections = list()
    for itemFlow in itemFlows:
        flow = dict()
        if 'realizing_connector_id' in itemFlow:
            for connectorPair in connectorPairs:
                if itemFlow['realizing_connector_id'] == connectorPair['id']:
                    # we now have the flow item data and the corresponding connector pair data
                    # the next two lines are for automation purposes
                    for informationType in ['information_source', 'information_target']:
                        for end in ['end1', 'end2']:
                            try:
                                # portInstance is our artificial port instance
                                portInstance = connectorPair[end]
                                # this function returns whether the information source / target element is either a block, a block instance or a port
                                typeOfInformationType = get_type_of_information_type(itemFlow[informationType], blocks, blockInstances, portInstances)
                                # to compare the source / target element with the port instance, we need e.g. the block the port instance belongs to
                                blockOrInstanceOrPort = get_block_or_instance_or_port_for_port_instance(portInstance, portInstances, blockInstances, typeOfInformationType, fullPorts, proxyPorts, ports, flowPorts, itemFlow[informationType], isPartOfsPorts)
                                # copy some information
                                flow['information_flow_id'] = itemFlow['information_flow_id']
                                flow['flow_item_id'] = itemFlow['flow_item_id']
                                flow['connector_id'] = connectorPair['id']
                                # determine if the port instance is source or target
                                if itemFlow[informationType] == blockOrInstanceOrPort and informationType == 'information_source':
                                    flow['source_id'] = portInstance
                                elif itemFlow[informationType] == blockOrInstanceOrPort and informationType == 'information_target':
                                    flow['target_id'] = portInstance
                                logging.debug('flowitem successfully sorted')
                            except:
                                logging.warn('your flowitem definition for information_flow_id %s is insufficient'%(itemFlow['information_flow_id']))
                                l=l+1
            if 'source_id' in flow.keys() and 'target_id' in flow.keys():
                k = k + 1
                flowsWithDirections.append(flow)
            else:
                logging.warning('source or target info in %s is missing' %flow)
                o += 1
        else:
            logging.warning('Item flow %s does not contain the property -realizingConnector-. Check MagicDraw for errors (maybe incompatible flow)' % itemFlow)
            m=m+1
    logging.info('of %s flowitems: \n %s flowitems were parsed correctly. \n %s flowitems are insufficiently defined (e.g. missing conveyed element) \n %s flowitems lack a realizing connector \n %s flowitems miss source or target.'%(n,k,l,m,o))
    return flowsWithDirections




def get_block_for_block_instance(sourceInstance, blockInstances):
    # get block of this instance
    for relation in blockInstances:
        if sourceInstance == relation['id']:
            block = relation['type']
            return block
    return None


def get_block_instance_for_port_instance(portInstance,instancesOfPorts):
    # get block instance of this port instance
    for instanceOfPort in instancesOfPorts:
        if portInstance == instanceOfPort['port_instance_id']:
            sourceInstance = instanceOfPort['block_instance_id']
            return sourceInstance
    return None


def get_port_for_port_instance(portInstance, instancesOfPorts):
    # get port for port instance
    for instanceOfPort in instancesOfPorts:
        if portInstance == instanceOfPort['port_instance_id']:
            port = instanceOfPort['port_id']
            return port
    return None


def get_block_or_instance_or_port_for_port_instance(portInstance, instancesOfPorts, blockInstances, elementType, fullPorts, proxyPorts, ports, flowPorts, itemFlowInformationType, isPartOfsPorts):
    if elementType == 'block':
        sourceInstance = get_block_instance_for_port_instance(portInstance,instancesOfPorts)
        # for nested ports, the sourceInstance is actually a port and not a block instance
        # -> we have to check if the source instance is a port
        # ! only fullPorts have a type of...
        # TODO: how is this for proxy ports and does it make sense to have a fullport nested on a proxyPort? How about proxy - proxy or other combinations?
        sourceInstanceIsFullPort = False
        for fullPort in fullPorts:
            if sourceInstance == fullPort['id']:
                sourceInstanceIsFullPort = True
                if itemFlowInformationType == fullPort['port_type_id']:
                    blockOrInstanceOrPort = fullPort['port_type_id']
                    return blockOrInstanceOrPort

        # sometimes, the source / target is not a block representing the type of a port as above, then it can be the block the port belongs to
        if sourceInstanceIsFullPort is True:
            for isPartOfsPort in isPartOfsPorts:
                if sourceInstance == isPartOfsPort['subclass_id'] and itemFlowInformationType == isPartOfsPort['superclass_id']:
                    blockOrInstanceOrPort = isPartOfsPort['superclass_id']
                    return blockOrInstanceOrPort

        # this is the block the port instance is connected to via the block instance
        blockOrInstanceOrPort = get_block_for_block_instance(sourceInstance, blockInstances)

    elif elementType == 'instance':
        sourceInstance = portInstance
        blockOrInstanceOrPort = get_block_instance_for_port_instance(portInstance,instancesOfPorts)
    elif elementType == 'port':
        blockOrInstanceOrPort = get_port_for_port_instance(portInstance, instancesOfPorts)
    return blockOrInstanceOrPort


##########################################################
##########               Activities             ##########
##########################################################

def get_activities(root):
    activities = list()
    for element in root.iter('node'):
        if element.get(xmi+'type') == 'uml:CallBehaviorAction':
            activity               = dict()
            activity['id']         = element.get(xmi+'id')
            pins                   = ''
            for argument in element.iter('argument'):
                pinid = argument.get(xmi + 'id')
                pins  = pins + pinid + ','
            for result in element.iter('result'):
                pinid = result.get(xmi + 'id')
                pins  = pins + pinid + ','
            try:
                activity['name'] = element.get('name')
            except:
                logging.debug('activity with id %s has no name'%(activity['id']))
            activity['pins'] = pins
            activities.append(activity)
    for element in root.iter('packagedElement'):
        if  element.get(xmi+'type') == 'uml:Activity':
            activity             = dict()
            activity['id']       = element.get(xmi+'id')
            try:
                activity['name'] = element.get('name')
            except:
                logging.debug('activity with id %s has no name'%(activity['id']))
            activities.append(activity)
    return activities


def get_activity_nodes(root):
    activity_nodes = list()
    types_of_activity_nodes = ['uml:InitialNode','uml:ActivityFinalNode',
                               'uml:JoinNode','uml:ForkNode',
                               'uml:CentralBufferNode','uml:DecisionNode',
                               'uml:MergeNode','uml:ActivityParameterNode']

    for packagedElement in root.iter('packagedElement'):
        if packagedElement.get(xmi + 'type') == 'uml:Activity':

            for nodetype in types_of_activity_nodes:

                for element in packagedElement.iter('node'):
                    if element.get(xmi+'type') == nodetype:
                        node             = dict()
                        node['id']       = element.get(xmi+'id')
                        node['nodetype'] = nodetype
                        node['type']     = element.get('type')
                        node['parent_activity_id'] = packagedElement.get(xmi + 'id')
                        try:
                            node['name'] = element.get('name')
                        except:
                            logging.debug('%s with id %s has no name'%(nodetype, element[xmi + 'id']))
                        activity_nodes.append(node)
    return activity_nodes


def get_hierarchic_activity_relations(root):
    hierarchic_activity_relations = list()
    types_of_activity_nodes = ['uml:InitialNode','uml:ActivityFinalNode',
                               'uml:JoinNode','uml:ForkNode',
                               'uml:CentralBufferNode','uml:DecisionNode',
                               'uml:MergeNode','uml:ActivityParameterNode','uml:CallBehaviorAction']

    for element in root.iter('packagedElement'):
        if element.get(xmi + 'type') == 'uml:Activity':
            for node in element.iter('node'):
                if node.get(xmi+'type') in types_of_activity_nodes:
                    relation              = dict()
                    relation['owning_id'] = element.get(xmi + 'id')
                    relation['owned_id']  = node.get(xmi + 'id')
                    hierarchic_activity_relations.append(relation)
    return hierarchic_activity_relations

def get_activity_instances(root):
    activity_instances = list()

    for element in root.iter('node'):
        if element.get(xmi + 'type') == 'uml:CallBehaviorAction':
            activity_instance = dict()
            if isinstance(element.get('behavior'), str):
                try:
                    activity_instance['class_activity'] = element.get('behavior')
                    activity_instance['instance_activity'] = element.get(xmi + 'id')
                    activity_instances.append(activity_instance)
                except:
                    pass
    return activity_instances


def get_activity_controlflows(root):
    control_flows = list()
    for element in root.iter('edge'):
        if element.get(xmi + 'type') == 'uml:ControlFlow':
            flow  = dict()
            flow['id']        = element.get(xmi + 'id')
            flow['source_id'] = element.attrib['source']
            flow['target_id'] = element.attrib['target']
            control_flows.append(flow)
    return control_flows


def get_act_controlflow_guards(root):
    guards = list()
    for element in root.iter('edge'):
        guard = dict()
        for guardelement in element.iter('guard'):
            for body in guardelement.iter('body'):
                try:
                    guard ['text']                      = body.text
                    guard ['connected_control_flow_id'] = element.get(xmi + 'id')

                except:
                    logging.debug('fml')
            guards.append(guard)
    return guards

def get_act_objectflows(root):
    oflows = list()
    for element in root.iter('edge'):
        if element.get(xmi + 'type') == 'uml:ObjectFlow':
            oflow = dict()
            oflow['name'] = element.get('name')
            oflow['source'] = element.get('source')
            oflow['target'] = element.get('target')
            oflow['id'] = element.get(xmi + 'id')
            oflows.append(oflow)


    return oflows

##########################################################
##########             State machines           ##########
##########################################################


def get_stm_states(root):
    states=list()
    for parentelement in root.iter('packagedElement'):
        if parentelement.get(xmi + 'type') == 'uml:StateMachine':
            state = dict()
            state['id']  =  parentelement.get(xmi + 'id')
            state['name']  = parentelement.get('name')
            states.append(state)
        for element in parentelement.iter('subvertex'):
            if element.get(xmi + 'type') == 'uml:State':
                state = dict()
                state['id'] = element.get(xmi + 'id')
                state['parentid'] = parentelement.get(xmi + 'id')
                state['name'] = element.get('name')
                states.append(state)
            for subelement in element.iter('region'):
                for subvertex in subelement.iter('subvertex'): #using iter on
                # subvertex twice will cause states to appear multiple times in the
                # json file. This is known and accepted, as the merge clause in
                # insert_SysML_in_neo4j is able to deal with multiple appearances.
                    if subvertex.get(xmi + 'type') == 'uml:State':
                        state = dict()
                        state['id'] = subvertex.get(xmi + 'id')
                        state['name'] = subvertex.get('name')
                        state['parentid'] = element.get(xmi + 'id')
                        states.append(state)
    return states

def get_stm_pseudostates(root):
    states=list()
    for parentelement in root.iter('packagedElement'):
        if parentelement.get(xmi + 'type') == 'uml:StateMachine':
            for element in parentelement.iter('subvertex'):
                if element.get(xmi + 'type') == 'uml:Pseudostate' or element.get(xmi + 'type') == 'uml:FinalState':
                    state = dict()
                    state['id']       = element.get(xmi + 'id')
                    state['name']     = element.get('name')
                    state['type']     = element.get(xmi + 'type')
                    state['parentid'] = parentelement.get(xmi + 'id')
                    states.append(state)

            for element in parentelement.iter('subvertex'):
                for subelement in element.iter('region'):
                    for subvertex in subelement.iter('subvertex'): #using iter on
                    # subvertex twice will cause states to appear multiple times in the
                    # json file. This is known and accepted, as the merge clause in
                    # insert_SysML_in_neo4j is able to deal with multiple appearances.
                        if subvertex.get(xmi + 'type') == 'uml:Pseudostate' or subvertex.get(xmi + 'type') == 'uml:FinalState':
                            state = dict()
                            state['id']       = subvertex.get(xmi + 'id')
                            state['name']     = subvertex.get('name')
                            state['parentid'] = element.get(xmi + 'id')
                            state['type']     = subvertex.get(xmi + 'type')
                            states.append(state)

                for subelement in element.iter('connectionPoint'):
                    state = dict()
                    state['id']       = subelement.get(xmi + 'id')
                    state['name']     = subelement.get('name')
                    state['type']     = subelement.get(xmi + 'type')
                    state['kind']     = subelement.get('kind')
                    state['parentid'] = element.get(xmi + 'id')
                    states.append(state)

    return states

def get_stm_transitions(root):
    transitions = list()
    for element in root.iter('transition'):
        transition = dict()
        transition['id'] = element.get(xmi + 'id')
        transition['source'] = element.get('source')
        transition['target'] = element.get('target')

        for trigger in element.iter('trigger'):
            transition['trigger_id'] = trigger.get(xmi + 'id')
            transition['trigger_event'] = trigger.get('event')
        for body in element.iter('body'):
            try:
                transition ['guard'] = body.text
            except:
                logging.debug('fml')
        transitions.append(transition)
    return transitions

def get_signals(root):
    # signals are required as triggers on state transitions and hence need to be retriebed from the model.
    signals = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi+'type') == 'uml:SignalEvent':
            signal = dict()
            signal['event'] = element.get(xmi + 'id')
            signal['id'] = element.get('signal')
            for signaliter in root.iter('nestedClassifier'):
                if signaliter.get(xmi + 'id') == signal['id']:
                    signal['name'] = signaliter.get('name')
            for transition in root.iter('transition'):
                for trigger in transition.iter('trigger'):
                    if signal['event'] == trigger.get('event'):
                        signal['event'] = transition.get(xmi + 'id')
            signals.append(signal)
    return signals

def get_stm_transition_activities(root):
    transition_activities = list()
    for transition in root.iter('transition'):
        for effect in transition.iter('effect'):
            if effect.get(xmi + 'type') == 'uml:Activity':
                for node in effect.iter('node'):
                    if node.get(xmi+'type') == 'uml:CallBehaviorAction':
                        transition_activity = dict()
                        transition_activity['activity_id'] = node.get('behavior')
                        transition_activity['transition_id'] = transition.get(xmi + 'id')
                        transition_activities.append(transition_activity)
    return transition_activities

def get_stm_nodes_activities(root):
    state_activities = list()
    list_of_act=['entry', 'doActivity', 'exit']
    for subvertex in root.iter('subvertex'):
        state_act_dict = dict()
        if subvertex.get(xmi + 'type') == 'uml:State':
            for actelt in list_of_act:
                for act in subvertex.iter(actelt):
                    state_act_dict[actelt + '_id'] = act.get(xmi + 'id')
                    state_act_dict[actelt + '_name'] = act.get('name')
                    state_act_dict['state_id'] =  subvertex.get(xmi + 'id')
                    for node in act.iter('node'):
                        if node.get(xmi + 'type') == 'uml:CallBehaviorAction':
                            state_act_dict[actelt + '_ref'] = node.get('behavior')

        if state_act_dict:
            state_activities.append(state_act_dict)
    return state_activities


##########################################################
##########            Requirements              ##########
##########################################################



def retrieve_requirements(root):
    requirements = list()
    for element in root.iter(sysml + 'Requirement'):
        requirement = dict()
        requirement['id']            = element.attrib["base_Class"]
        try:
            requirement["Text"] = element.attrib["Text"]
        except:
            pass
        try:
            requirement["Requirement_ID_in_MagicDraw"] = element.attrib["Id"]
        except:
            pass
        try:
            requirement["Verify_Method"] = element.attrib["verifyMethod"]
        except:
            pass
        try:
            requirement['TracedTo'] = element.attrib['TracedTo']
        except:
            pass

        for classElement in root.iter('packagedElement'):
            if classElement.get(xmi + 'id') == element.get('base_Class'):
                try:
                    requirement['title'] = classElement.get('name')
                except:
                    pass
        for nestedClassifier in root.iter('nestedClassifier'):
           if nestedClassifier.get(xmi + 'id') == element.get('base_Class'):
               try:
                   requirement['title'] = nestedClassifier.get('name')
               except:
                   pass


        requirements.append(requirement)
    logging.info('Requirements added via get_requirements_from_mdxml')
    return requirements


def retrieve_requirements_hierarchy(root):
    # SysML requirements are split in two entities: the sysml:requirement element
    # which holds most of the information and the baseclass packagedElement,
    # which only holds the name and hierarchy.
    # It is not possible to identify a baseclass element of a sysml requirement
    # without having  its sysml:requirement element.
    relations = list()
    for sysmlRequirement in root.iter(sysml + 'Requirement'):
        for classElement in root.iter('packagedElement'):
            if classElement.get(xmi + 'id') == sysmlRequirement.get('base_Class'):
                for nestedClassifier in classElement.iter('nestedClassifier'):
                    relation=dict()
                    relation['container'] = sysmlRequirement.get('base_Class')
                    relation['contained'] = nestedClassifier.get(xmi + 'id')
                    relations.append(relation)
    logging.info("requirements hierarchy retrieved with retrieve_requirements_hierarchy")
    return relations


def retrieve_requirements_traces(requirements, root):
    # MD saves the tracedTo relations all as one string, separated by a whitespace
    # to make something useful out of this single entries have to be formed
    reqTraces = list()
    for requirement in requirements:
        if 'TracedTo' in requirement.keys():
            tracestring = requirement['TracedTo']
            tracelist   = tracestring.split()
            for trace in tracelist:
                tracedict = dict()
                tracedict['source']  = requirement['id']
                tracedict ['target'] =trace
                reqTraces.append(tracedict)
    logging.info('retrieve_requirements_traces executed')
    return reqTraces

def retrieve_requirements_derives(root):
    reqDerives = list()
    for derive in root.iter(sysml + 'DeriveReqt'):
        reqDerive = dict()
        reqDerive['id'] = derive.get('base_Abstraction')
        for abstraction in root.iter('packagedElement'):
            if abstraction.get(xmi + 'id') == reqDerive['id']:
                for client in abstraction.iter('client'):
                    reqDerive['clientID'] = client.get(xmi + 'idref')
                for supplier in abstraction.iter('supplier'):
                    reqDerive['supplierID'] = supplier.get(xmi + 'idref')
                try:
                    reqDerive['name'] =  abstraction.get('name')
                except: pass
        reqDerives.append(reqDerive)
    logging.info('retrieve_requirements_derives executed')
    return reqDerives

def retrieve_requirements_refines(root):
    reqRefines = list()
    for refinexml in root.iter(sysml + 'Refine'):
        refine = dict()
        refine['id'] = refinexml.get('base_Abstraction')
        for abstraction in root.iter('packagedElement'):
            if abstraction.get(xmi + 'id') == refine['id']:
                for client in abstraction.iter('client'):
                    refine['clientID'] = client.get(xmi + 'idref')
                for supplier in abstraction.iter('supplier'):
                    refine['supplierID'] = supplier.get(xmi + 'idref')
                try:
                    refine['name'] =  abstraction.get('name')
                except: pass
        reqRefines.append(refine)
    logging.info('retrieve_requirements_refines executed')
    return reqRefines

def get_use_cases(root):
    useCases = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi + "type") == "uml:UseCase":
            useCase = dict()
            useCase['id'] = element.attrib[xmi + "id"]
            try:

                useCase['name']= element.attrib["name"]
            except:
                logging.debug('Use Case has no name')
            useCases.append(useCase)
            logging.debug('Use Case added via get_use_cases')
    logging.info('get_use_cases executed')
    return useCases

def get_actors(root):
    actors = list()
    for actorelement in root.iter('packagedElement'):
        if actorelement.get(xmi + 'type') == 'uml:Actor':
            actor = dict()
            actor['id'] = actorelement.get(xmi + 'id')
            actor['name'] = actorelement.get('name')
            actors.append(actor)

    logging.info('get_actors executed')
    return actors

def get_actor_uc_relations(root, actors):
    associations = list()
    for actor in actors:
        for ownedEnd in root.iter('ownedEnd'):
            if ownedEnd.get('type') == actor['id']:
                association = dict()
                associationId=ownedEnd.get('association')
                association['actor'] = actor['id']

                for ownedEnd2 in root.iter('ownedEnd'):
                    if ownedEnd2.get('association') == associationId and ownedEnd2.get(xmi + 'id') != ownedEnd.get(xmi + 'id'):
                        association['useCase'] = ownedEnd2.get('type')
                        associations.append(association)
        logging.info('get_actor_uc_relations executed')
    return associations

def get_usecase_includes(root):
    includes_list = list()
    for useCase in root.iter('packagedElement'):
        if useCase.get(xmi + "type") == "uml:UseCase":
            for include in useCase.iter('include'):
                includes = dict()
                includes['parent'] = useCase.get(xmi + 'id')
                includes['child']  = include.get('addition')
                includes_list.append(includes)
    logging.info('get_usecase_includes executed')
    return includes_list

def get_usecase_extensionpoints(root):
    extensionPoints_list = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi + "type") == "uml:UseCase":
            for extensionPoint in element.iter('extensionPoint'):
                extP = dict()
                extP['useCase'] = element.get(xmi + 'id')
                extP['id']      = extensionPoint.get(xmi + 'id')
                extP['name']    = extensionPoint.get('name')
                extensionPoints_list.append(extP)

    logging.info('get_usecase_extensionpoints executed')
    return extensionPoints_list

def get_usecase_extensions(root):
    extensions_list = list()
    for element in root.iter('packagedElement'):
        if element.get(xmi + "type") == "uml:UseCase":
            for extension in element.iter('extend'):
                extend = dict()
                extend['useCase']           = element.get(xmi + 'id')
                for extP in extension.iter('extensionLocation'):
                    extend['extensionPoint'] = extP.get(xmi + 'idref')
                    extend['parentUseCase']  = extension.get('extendedCase')
                    extend['id']             = extension.get(xmi  + 'id')
                extensions_list.append(extend)
    logging.info('get_usecase_extensions executed')
    return extensions_list


def get_allocations(root):
    allocations = list()
    for element in root.iter(sysml + 'Allocate'):
        abstractionId = element.get('base_Abstraction')
        for abstraction in root.iter('packagedElement'):
            if abstraction.get(xmi + 'id') == abstractionId:
                allocate = dict()
                for client in abstraction.iter('client'): # requirement
                    allocate['client']   = client.get(xmi + 'idref')

                for supplier in abstraction.iter('supplier'): #usecase
                    allocate['supplier'] = supplier.get(xmi + 'idref')
                allocations.append(allocate)
        logging.info('get_allocations executed')
    return allocations

def get_verify_relations(root):
    verifies = list()
    for element in root.iter(sysml + 'Verify'):
        abstractionId =  element.get('base_Abstraction')
        for abstraction in root.iter('packagedElement'):
            if abstraction.get(xmi + 'id') == abstractionId:
                verify = dict()
                for client in abstraction.iter('client'): # requirement
                    verify['testcase']   = client.get(xmi + 'idref')

                for supplier in abstraction.iter('supplier'): #usecase
                    verify['requirement'] = supplier.get(xmi + 'idref')
                verifies.append(verify)
    logging.info('get_verify_relations executed')
    return verifies

def get_test_cases(root):
    testCases = list()
    for tc in root.iter(sysml + 'TestCase'):
        tcId=tc.get('base_Behavior')
        for tcActivity in root.iter('packagedElement'):
            if tcActivity.get(xmi + 'id') == tcId:
                testCase = dict()
                testCase['name'] = tcActivity.get('name')
                testCase['id']   = tcActivity.get(xmi + 'id')
                testCases.append(testCase)
    logging.info('get_test_cases executed')
    return testCases

def get_satisfy_relations(root):
    satisfys = list()
    for sat in root.iter(sysml+'Satisfy'):
        for abstraction in root.iter('packagedElement'):
            if sat.get('base_Abstraction') == abstraction.get(xmi + 'id'):
                satisfy=dict()
                for client in abstraction.iter('client'):
                    satisfy['block']       = client.get(xmi + 'idref')

                for supplier in abstraction.iter('supplier'):
                    satisfy['requirement'] = supplier.get(xmi + 'idref')
                satisfys.append(satisfy)
    logging.info('get_satisfy_relations executed')
    return satisfys


def main_function():
    path = magicdraw_path
    file = magicdraw_file
    pathToFile = join(path,file)

    tree = ET.parse(pathToFile)
    root = tree.getroot()

    ########################################################################################################################
    # get data from SysML model

    get_prefixes(pathToFile)

    # bdd
    # sysmlBlocks                             = get_sysml_blocks_from_mdxml(root)
    # blocks                                  = get_blocks_from_mdxml(root, sysmlBlocks)
    # isPartOfsBlocks                         = get_is_part_of_relations_for_blocks_from_mdxml(root)
    # generalizations                         = get_generalization_from_mdxml(root)


    # ibd
    # blockInstances                          = get_block_instances_from_mdxml(root=root, sysmlProperties=sysmlBlocks)
    # sysmlFullPorts                          = get_sysml_ports_from_mdxml(root, portType="FullPort")
    # sysmlProxyPorts                         = get_sysml_ports_from_mdxml(root, portType="ProxyPort")
    # sysmlFlowPorts                          = get_sysml_ports_from_mdxml(root, portType="FlowPort")
    # fullPorts, proxyPorts, ports, flowPorts = get_ports_from_mdxml(root, sysmlFullPorts, sysmlProxyPorts, sysmlFlowPorts)
    # nestedConnectorEnds                     = get_sysml_nested_connector_ends_from_mdxml(root)
    # connectors                              = get_connectors_from_mdxml(root)
    # nestedPorts                             = get_nested_port_couples_from_mdxml(connectors, fullPorts, proxyPorts, ports, flowPorts)
    # isPartOfsPorts                          = get_is_part_of_relations_for_ports_from_mdxml(root, nestedPorts)
    # portInstances, connectorPairs           = get_port_instances_and_pairing(connectors, isPartOfsPorts, nestedPorts, blockInstances, nestedConnectorEnds)
    # itemFlows                               = get_sysml_itemflows_from_mdxml(root)
    # itemFlowsWithDirections                 = determine_itemflow_direction(itemFlows, connectorPairs, blocks, blockInstances, portInstances, connectors, nestedConnectorEnds, fullPorts, proxyPorts, ports, flowPorts, isPartOfsPorts)

    # act
    # activities                              = get_activities(root)
    # activityInstances                       = get_activity_instances(root)
    # activityNodes                           = get_activity_nodes(root)
    # hierarchicActivityRelations             = get_hierarchic_activity_relations(root)
    # activityControlFlows                    = get_activity_controlflows(root)
    # activityControlFlowGuards               = get_act_controlflow_guards(root)
    # activityOFlows                          = get_act_objectflows(root)

    # stm
    states                                  = get_stm_states(root)
    pseudostates                            = get_stm_pseudostates(root)
    transitions                             = get_stm_transitions(root)
    stateActivities                         = get_stm_nodes_activities(root)
    signals                                 = get_signals(root)
    transitionActivities                    = get_stm_transition_activities(root)

    # Requirements - Use Cases
    # requirements                            = retrieve_requirements(root)
    # requirementsHierarchy                   = retrieve_requirements_hierarchy(root)
    # requirementsTraces                      = retrieve_requirements_traces(requirements, root)
    # requirementsDerives                     = retrieve_requirements_derives(root)
    # requirementsRefines                     = retrieve_requirements_refines(root)
    # useCases                                = get_use_cases(root)
    # actors                                  = get_actors(root)
    # uc_associations                         = get_actor_uc_relations(root, actors)
    # includes_list                           = get_usecase_includes(root)
    # extensionPoints_list                    = get_usecase_extensionpoints(root)
    # extensions_list                         = get_usecase_extensions(root)
    # verifyRelations                         = get_verify_relations(root)
    # allocateRelations                       = get_allocations(root)
    # testCases                               = get_test_cases(root)
    # satisfyRelations                        = get_satisfy_relations(root)

    ########################################################################################################################
    # save data in json files

    # bdd
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_sysml_blocks.json", information=sysmlBlocks)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_blocks.json", information=blocks)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_is_part_ofs_blocks.json", information=isPartOfsBlocks)

    # ibd
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_block_instances.json", information=blockInstances)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_full_ports.json", information=fullPorts)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_proxy_ports.json", information=proxyPorts)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_ports.json", information=ports)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_flow_ports.json", information=flowPorts)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_generalizations.json", information=generalizations)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_nested_connector_ends.json", information=nestedConnectorEnds)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_is_part_ofs_ports.json", information=isPartOfsPorts)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_connectors.json", information=connectors)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_nested_ports.json",information=nestedPorts)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_item_flows.json", information=itemFlows)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_port_instances.json", information=portInstances)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_connector_pairs.json", information=connectorPairs)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_item_flows_with_directions.json", information=itemFlowsWithDirections)

    # # act
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_activities.json', information=activities)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_activity_instances.json', information=activityInstances)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_activity_nodes.json', information=activityNodes)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_hierarchic_activity_relations.json', information=hierarchicActivityRelations)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_activity_control_flows.json', information=activityControlFlows)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_activity_control_flow_guards.json', information=activityControlFlowGuards)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_activity_o_flows.json', information=activityOFlows)

    # stm
    custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_stm_states.json', information=states)
    custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_stm_pseudostates.json', information=pseudostates)
    custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_stm_transitions.json', information=transitions)
    custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_stm_signals.json', information=signals)
    custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_stm_transition_activities.json', information=transitionActivities)
    custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_state_activities.json', information=stateActivities)
    #
    # #req
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_requirements.json", information=requirements)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_requirements_hierarchy.json", information=requirementsHierarchy)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_requirements_traces.json", information=requirementsTraces)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_requirements_derives.json", information=requirementsDerives)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_requirements_refines.json", information=requirementsRefines)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename='magicdraw_use_cases.json', information=useCases)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_actors.json", information=actors)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_usecase_associations.json", information=uc_associations)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_usecase_includes.json", information=includes_list)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_usecase_extension_points.json", information=extensionPoints_list)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_usecase_extensions.json", information=extensions_list)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_allocate_relations.json", information=allocateRelations)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_verify_relations.json", information=verifyRelations)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_test_cases.json", information=testCases)
    # custom_functions.dump_retrieved_information(path=magicdraw_path, filename="magicdraw_satisfy_relations.json", information=satisfyRelations)


if __name__ == '__main__':
    main_function()
