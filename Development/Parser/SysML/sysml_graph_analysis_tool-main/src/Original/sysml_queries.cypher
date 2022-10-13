// cypher queries for the MBSE2DL project

// define the searchterm parameter
:PARAM searchterm => 'BCR 1 Current'

//producttree
MATCH path = (m:BLOCK {name: 'ADCS'}) <-[:IS_PART_OF*0..]-(n) <-[:IS_PART_OF]- (o)
RETURN n.name as containingElement, o.name as containedElement  ORDER by n.name

//

//show all containers for a certain FLOWITEM
MATCH path = (m:FLOWITEM {name: $searchterm}) -[:IS_PART_OF*]-> (n:FLOWITEM)
RETURN path
//provide the searchterm FLOWITEM and all its containing FLOWITEMs in a single mapping:
MATCH (fi:FLOWITEM {name: $searchterm})
RETURN fi
UNION
MATCH(fisource:FLOWITEM {name: $searchterm}) -[:IS_PART_OF*]-> (fi:FLOWITEM)
RETURN fi

//components processing searchterm
MATCH (fi:FLOWITEM {name: $searchterm})-[:FLOWS_IN]->(:HYPERNODE)-[:FLOWS]-(port:PORT)
OPTIONAL MATCH (port) -[:IS_PART_OF]-> (processingPart)
RETURN port.name as PortName, processingPart.name as ProcessingPart

// show all components requiring the searchterm FLOWITEM or one of its containers
//components requiring FLOWITEM
CALL{MATCH (fi:FLOWITEM {name: $searchterm})
RETURN fi
UNION
MATCH(fisource:FLOWITEM {name: $searchterm}) -[:IS_PART_OF*]-> (fi:FLOWITEM)
RETURN fi}
MATCH (fi)-[:FLOWS_IN]->(:HYPERNODE)-[:FLOWS]->(port:PORT)
OPTIONAL MATCH (port) -[:IS_PART_OF]->(processingPart)
RETURN fi.name as DataForm, port.name as PortName, processingPart.name as ProcessingElement

//datapath searchterm
CALL{MATCH (fi:FLOWITEM {name: 'EPS Battery Temperature 1'})
RETURN fi
UNION
MATCH(fisource:FLOWITEM {name: 'EPS Battery Temperature 1'}) -[:IS_PART_OF*]-> (fi:FLOWITEM)
RETURN fi}
MATCH(fi)-[:FLOWS_IN]->(hpn:HYPERNODE)
WITH fi, hpn
MATCH path = (source:BLOCK:INSTANCE)-[:FLOWS*1..2]->(hpn)-[FLOWS*1..2]->(target:BLOCK:INSTANCE)
return fi, path

//datapath searchterm table
CALL{MATCH (fi:FLOWITEM {name: 'EPS Battery Temperature 1'})
RETURN fi
UNION
MATCH(fisource:FLOWITEM {name: 'EPS Battery Temperature 1'}) -[:IS_PART_OF*]-> (fi:FLOWITEM)
RETURN fi}
MATCH(fi)-[:FLOWS_IN]->(hpn:HYPERNODE)
WITH fi, hpn
MATCH path = (source:BLOCK:INSTANCE)-[:FLOWS*1..2]->(hpn)-[FLOWS*1..2]->(target:BLOCK:INSTANCE)
return DISTINCT fi.name as processedElement, source.name as source, target.name as target

//simplified datapath
CALL {
    MATCH (fi:FLOWITEM) WHERE fi.name CONTAINS $searchterm
    RETURN fi
    UNION
    MATCH (fivalue:FLOWITEM)-[:IS_PART_OF*]->(ficontainer)
    WHERE fivalue.name contains $searchterm
    RETURN ficontainer AS fi
}
    MATCH path = (s:BLOCK)<-[:IS_INSTANCE_OF]-(sb:BLOCK:INSTANCE)-[:FLOWS*1..2]->(hpn:HYPERNODE)-[FLOWS*1..2]->(tb:BLOCK:INSTANCE)-[:IS_INSTANCE_OF]->(t:BLOCK)
    WHERE EXISTS {(hpn)<-[:FLOWS_IN]-(fi)}
    CALL apoc.create.vRelationship(s,'vFLOWS',{dataform:fi.name, connector: hpn.id},t) YIELD rel
    RETURN s, rel, t
