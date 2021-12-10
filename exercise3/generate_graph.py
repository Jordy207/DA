from networkx.generators.random_graphs import erdos_renyi_graph
import random

seed = 8122021

# check if every node has at least one edge by flattening the list of edge tuples,
# convert it to a set and see if it as long as the node list.
def is_connected(graph):
    return len(set((sum(graph.edges, ())))) == len(graph.nodes)

def create_graph(n):
    # p value dictates the sparsity of the graph
    p = random.uniform(0.25, 0.75)
    g = erdos_renyi_graph(n, p, seed=seed, directed=False)
    while not is_connected(g):
        p = random.uniform(0.25, 0.75)
        g = erdos_renyi_graph(n, p, seed=seed, directed=False)
    return g

n = 10
graph = create_graph(n)
name = "test"

weights = list(range(1, len(graph.edges)+1))
random.shuffle(weights)
with open("graphs/graph_{}.txt".format(name), 'w') as f:
    f.write(str(n))
    f.write('\n')
    f.write(str(len(graph.edges)))
    f.write('\n')
    for line in graph.edges:
        f.write(str(line)+" "+str(weights.pop(0)))
        f.write('\n')