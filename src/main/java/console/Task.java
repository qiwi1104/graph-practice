package console;

import model.components.Edge;
import model.components.Vertex;
import model.graph.DirectedGraph;
import model.graph.Graph;

import java.util.*;

public class Task {
    private static final String FOLDER = System.getProperty("user.dir") + "/src/main/resources/";

    private static Graph graph;

    // Ia (2)
    public static void isolatedVertices(Graph graph1) {
        graph = graph1;

        /*
         * Searches for isolated vertices
         * */
        Set<Vertex> vertexSet = new HashSet<>();
        for (Map.Entry<Vertex, List<Vertex>> entry : graph.getAdjacencyList().entrySet()) {
            if (entry.getValue().isEmpty())
                vertexSet.add(entry.getKey());
        }

        if (vertexSet.isEmpty()) {
            System.out.println("There are no isolated vertices");
        } else {
            System.out.print("Isolated vertices:");
            vertexSet.forEach(System.out::println);
        }
    }

    // Ia (3)
    public static void adjacentVertices(Graph graph1, String label) {
        graph = graph1;
        Set<Vertex> adjacentVertices = new HashSet<>();

        graph.getAdjacencyList().forEach((key, value) -> value.forEach(v -> {
            if (v.getLabel().equals(label)) {
                adjacentVertices.add(key);
            }
            if (key.getLabel().equals(label)) {
                adjacentVertices.addAll(value);
            }
        }));

        System.out.println("Vertices adjacent to " + label + ": ");
        adjacentVertices.forEach(v -> System.out.println(v.getLabel()));
    }

    // Ib (4)

    public static Graph graphComplement(Graph graph1) {
        DirectedGraph complementGraph = new DirectedGraph();

        Map<Vertex, List<Vertex>> adjacencyList = graph1.getAdjacencyList();

        Map<Vertex, List<Vertex>> complementGraphAdjacencyList = new HashMap<>();

        List<Vertex> vertices = new ArrayList<>(graph1.getAdjacencyList().keySet());

        for (Vertex vertex : vertices) {
            complementGraphAdjacencyList.put(vertex, new ArrayList<>(vertices));
        }

        for (Map.Entry<Vertex, List<Vertex>> entry : adjacencyList.entrySet()) {
            for (Vertex b : new ArrayList<>(entry.getValue())) {
                complementGraphAdjacencyList.get(entry.getKey()).remove(b);
            }
        }

        complementGraph.setAdjacencyList(complementGraphAdjacencyList);

        return complementGraph;
    }

    // II (5)
    private static boolean BFS(Map<Vertex, List<Vertex>> adjacencyList, Vertex start) {
        Queue<Vertex> verticesQueue = new ArrayDeque<>();
        Set<Vertex> visitedVertices = new HashSet<>();

        verticesQueue.add(start);

        Vertex currentVertex;

        while (!verticesQueue.isEmpty()) {
            currentVertex = verticesQueue.remove();

            if (!visitedVertices.contains(currentVertex)) {
                verticesQueue.addAll(adjacencyList.get(currentVertex));
                visitedVertices.add(currentVertex);
            }
        }

        return visitedVertices.containsAll(adjacencyList.keySet());
    }

    private static Graph reverseEdges(Map<Vertex, List<Vertex>> adjacencyList) {
        DirectedGraph reversedGraph = new DirectedGraph();

        for (Map.Entry<Vertex, List<Vertex>> entry : adjacencyList.entrySet()) {
            for (Vertex vertex : entry.getValue()) {
                if (!reversedGraph.getAdjacencyList().containsKey(vertex)) {
                    reversedGraph.addVertex(vertex.getLabel());
                }

                if (!reversedGraph.getAdjacencyList().containsKey(entry.getKey())) {
                    reversedGraph.addVertex(entry.getKey().getLabel());
                }

                reversedGraph.addEdge(vertex.getLabel(), entry.getKey().getLabel());
            }
        }

        return reversedGraph;
    }

    public static void isStronglyConnected(Graph graph) {
        Map<Vertex, List<Vertex>> adjacencyList = graph.getAdjacencyList();
        Vertex start = adjacencyList.entrySet().stream()
                .findAny()
                .get()
                .getKey();

        boolean allVerticesReachable = BFS(adjacencyList, start);

        if (!allVerticesReachable) {
            System.out.println("This graph is not strongly connected");
        } else {
            DirectedGraph reversedGraph = (DirectedGraph) reverseEdges(adjacencyList);

            allVerticesReachable = BFS(reversedGraph.getAdjacencyList(), start);

            if (allVerticesReachable) {
                System.out.println("This graph is strongly connected");
            } else {
                System.out.println("This graph is not strongly connected");
            }
        }
    }

    // II (6)
    private static Map<Vertex, List<Vertex>> removeEdges(Graph graph,
                                                         List<Edge> edgesToRemove) {
        Graph graphCopy = graph.clone();

        for (Edge edge : edgesToRemove) {
            graphCopy.removeEdge(edge.getFrom().getLabel(), edge.getTo().getLabel());
        }

        return new HashMap<>(graphCopy.getAdjacencyList());
    }

    private static List<Vertex> DFS(Map<Vertex, List<Vertex>> adjacencyList,
                                    Vertex u, Vertex v,
                                    List<Vertex> visitedVertices) {
        if (visitedVertices.contains(v)) {
            return visitedVertices;
        }

        if (!visitedVertices.contains(u)) {
            visitedVertices.add(u);

            for (Vertex adjacentVertex : adjacencyList.get(u)) {
                System.out.println(u.getLabel());
                DFS(adjacencyList, adjacentVertex, v, visitedVertices);
            }
        }

        return visitedVertices;
    }

    public static void isReachable(Graph graph, Vertex u, Vertex v, List<Edge> edgesToRemove) {
        List<Vertex> visitedVertices = new ArrayList<>();

        Map<Vertex, List<Vertex>> adjacencyList = removeEdges(graph, edgesToRemove);

        visitedVertices = DFS(adjacencyList, u, v, visitedVertices);

        if (visitedVertices.contains(v)) {
            System.out.println("Reachable");
        } else {
            System.out.println("Unreachable");
        }
    }

    // III (7)
    static class Subset {
        int parent;
        int rank;

        public Subset(int parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }

    private static void union(List<Subset> subsets, int x, int y) {
        int rootX = findRoot(subsets, x);
        int rootY = findRoot(subsets, y);

        if (subsets.get(rootY).rank < subsets.get(rootX).rank) {
            subsets.get(rootY).parent = rootX;
        } else if (subsets.get(rootX).rank < subsets.get(rootY).rank) {
            subsets.get(rootX).parent = rootY;
        } else {
            subsets.get(rootY).parent = rootX;
            subsets.get(rootX).rank++;
        }
    }

    private static int findRoot(List<Subset> subsets, int i) {
        if (subsets.get(i).parent == i) {
            return subsets.get(i).parent;
        }

        subsets.get(i).parent = findRoot(subsets, subsets.get(i).parent);

        return subsets.get(i).parent;
    }

    private static void kruskals(int V, List<Edge> edges) {
        int j = 0;
        int noOfEdges = 0;

        List<Subset> subsets = new ArrayList<>(V);
        List<Edge> results = new ArrayList<>(V);

        for (int i = 0; i < V; i++) {
            subsets.add(new Subset(i, 0));
        }

        while (noOfEdges < V - 1 && j < V - 1) {

            // Pick the smallest edge. And increment
            // the index for next iteration
            Edge nextEdge = edges.get(j);
            int x = findRoot(subsets, Integer.parseInt(nextEdge.getFrom().getLabel()));
            int y = findRoot(subsets, Integer.parseInt(nextEdge.getTo().getLabel()));

            // If including this edge doesn't cause cycle,
            // include it in result and increment the index
            // of result for next edge
            if (x != y) {
                results.add(noOfEdges, nextEdge);
                union(subsets, x, y);
                noOfEdges++;
            }

            j++;
        }

        // Print the contents of result[] to display the
        // built MST
        System.out.println("These are the edges of the constructed MST:");

        int minCost = 0;

        for (int i = 0; i < noOfEdges; i++) {
            System.out.println(results.get(i).getFrom().getLabel() + " -- "
                    + results.get(i).getTo().getLabel() + " == "
                    + results.get(i).getWeight());

            minCost += results.get(i).getWeight();
        }

        System.out.println("Total cost of MST: " + minCost);
    }

    public static void MST(Graph graph) {
        List<Edge> edgeList = graph.getEdgeList();

        edgeList.sort(Comparator.comparingInt(Edge::getWeight));

        kruskals(edgeList.size(), edgeList);
    }

    //    IVa (8)
    public static boolean dijkstra(Graph graph, Vertex start, Vertex end, int l) {
        Map<Vertex, List<Vertex>> adjacencyList = graph.getAdjacencyList();

        List<Vertex> unvisitedVertices = new ArrayList<>(adjacencyList.keySet());

        Map<Vertex, Integer> distances = new HashMap<>();
        for (Vertex vertex : unvisitedVertices) {
            distances.put(vertex, Integer.MAX_VALUE);
        }

        distances.put(start, 0);

        Vertex currentVertex = start;

        do {
            List<Vertex> adjacentVertices = adjacencyList.get(currentVertex);

            Vertex finalCurrentVertex = currentVertex;

            Optional<Edge> edgeOptional = graph.getEdgeList().stream()
                    .filter(e -> e.getFrom().equals(finalCurrentVertex))
                    .filter(e -> unvisitedVertices.contains(e.getTo()))
                    .min(Comparator.comparingInt(Edge::getWeight));

            if (edgeOptional.isEmpty()) {
                break;
            } else {
                Vertex minWeightVertex = edgeOptional.get().getTo();

                for (Edge edge : graph.getEdgeList()) {
                    if (edge.getFrom().equals(currentVertex) && adjacentVertices.contains(edge.getTo())) {
                        distances.put(edge.getTo(), edge.getWeight() + distances.get(currentVertex));
                    }
                }

                unvisitedVertices.remove(currentVertex);

                currentVertex = minWeightVertex;
            }
        } while (!unvisitedVertices.isEmpty() || currentVertex.equals(end));

        return distances.get(end) <= l;
    }

    //    IVb (8)
    public static int[][] floyd(Graph graph, Vertex u, Vertex v1, Vertex v2) {
        int size = graph.getAdjacencyList().keySet().size();
        int[][] dist = new int[size][size];

        for (int[] ints : dist) {
            Arrays.fill(ints, Integer.MAX_VALUE);
        }

        for (Edge edge : graph.getEdgeList()) {
            int from = Integer.parseInt(edge.getFrom().getLabel());
            int to = Integer.parseInt(edge.getTo().getLabel());

            dist[from - 1][to - 1] = edge.getWeight();
        }

        for (Vertex vertex : graph.getAdjacencyList().keySet()) {
            int vertexLabel = Integer.parseInt(vertex.getLabel());

            dist[vertexLabel - 1][vertexLabel - 1] = 0;
        }

        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    //    IVc (8)
    private static int[] bellmanFord(Graph graph, Vertex vertex, int V, int E) {
        Edge[] edges = graph.getEdgeList().toArray(new Edge[0]);

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[Integer.parseInt(vertex.getLabel()) - 1] = 0;

        for (int i = 1; i < V; i++) {
            for (int j = 0; j < E; j++) {
                int u = Integer.parseInt(edges[j].getFrom().getLabel()) - 1;
                int v = Integer.parseInt(edges[j].getTo().getLabel()) - 1;
                int weight = edges[j].getWeight();

                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                }
            }
        }

        boolean negativeCycles = false;

        for (int j = 0; j < E; j++) {
            int u = Integer.parseInt(edges[j].getFrom().getLabel()) - 1;
            int v = Integer.parseInt(edges[j].getTo().getLabel()) - 1;
            int weight = edges[j].getWeight();

            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                negativeCycles = true;
                break;
            }
        }

        if (negativeCycles) {
            System.out.println("Contains negative cycles.");
            dist = new int[0];
        }

        return dist;
    }

    public static void bellmanFord(Graph graph) {
        int V = graph.getAdjacencyList().keySet().size();
        int E = graph.getEdgeList().size();

        for (Vertex vertex : graph.getAdjacencyList().keySet()) {
            System.out.println(Arrays.toString(bellmanFord(graph, vertex, V, E)));
        }
    }
}
