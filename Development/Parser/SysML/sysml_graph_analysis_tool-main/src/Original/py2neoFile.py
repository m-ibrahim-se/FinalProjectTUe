from py2neo import Graph


def test(graph, label):
    # get full nodes with a specific label
    # query = """MATCH (n:{}) RETURN DISTINCT n LIMIT 10""".format(label)
    # insertQuery = """  ;"""
    # graph.run(insertQuery)
    deleteQuery = """MATCH (c:COACH{name:"Mohammad Ibrahim"}) DELETE c"""
    graph.run(deleteQuery)
    query = """MATCH (n:{}) RETURN COUNT(n) AS COUNT""".format(label)
    nodes = graph.run(query).data()
    print("##### NODES COUNTS #####")
    print(nodes)

    ## get all properties for nodes with a specific label
    # properties = [x['properties'] for x in graph.run("""MATCH (n:{}) UNWIND keys(n) AS properties RETURN DISTINCT properties""".format(label)).data()]
    # print("##### PROPERTIES #####")
    # print(properties)
    ## get the first property in "properties" list for nodes with a specific label
    # nodes = [x['node'] for x in graph.run("""MATCH (n:{}) RETURN DISTINCT n.{} AS node LIMIT 10""".format(label, properties[0])).data()]
    # print("##### PROPERTY OF NODES #####")
    # print(nodes)


if __name__ == "__main__":
    port = '7687'  # input("Enter Neo4j DB Port: ")
    user = 'neo4j'  # input("Enter Neo4j DB Username: ")
    pswd = 'mipneo4jdb'  # input("Enter Neo4j DB Password: ")
    try:
        graph = Graph('bolt://localhost:' + port, auth=(user, pswd))
        print("Connected to the local graph database.")
        label = 'COACH'  # input("Enter a label in your database, verbatim: ")
        #test(graph, label)
    except:
        print("Could not connect to the local graph database.")
        quit()