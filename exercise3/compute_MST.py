import numpy as np

filename = "graphs/graph_15_nodes.txt"

with open(filename) as file:
    lines = file.readlines()
    lines = [line.rstrip() for line in lines]
    nodes = int(lines.pop(0).split(" ")[0])
    incidence_matrix = np.ones((nodes, nodes)) * float('inf')
    for line in lines:
        edge = [int(x) for x in line.split(" ")]
        incidence_matrix[edge[0], edge[1]] = edge[2]

# Python implementation for Kruskal's
# algorithm

# Find set of vertex i
def find(i):
	while parent[i] != i:
		i = parent[i]
	return i

# Does union of i and j. It returns
# false if i and j are already in same
# set.
def union(i, j):
	a = find(i)
	b = find(j)
	parent[a] = b

# Finds MST using Kruskal's algorithm
def kruskalMST(cost):
	mincost = 0 # Cost of min MST

	# Initialize sets of disjoint sets
	for i in range(V):
		parent[i] = i

	# Include minimum weight edges one by one
	edge_count = 0
	while edge_count < V - 1:
		min = INF
		a = -1
		b = -1
		for i in range(V):
			for j in range(V):
				if find(i) != find(j) and cost[i][j] < min:
					min = cost[i][j]
					a = i
					b = j
		union(a, b)
		print('Edge {}:({}, {}) cost:{}'.format(edge_count, a, b, min))
		edge_count += 1
		mincost += min

	print("Minimum cost= {}".format(mincost))

V = nodes
INF = float('inf')
parent = [i for i in range(V)]

# Print the solution
kruskalMST(incidence_matrix)

# This code is contributed by ng24_7
