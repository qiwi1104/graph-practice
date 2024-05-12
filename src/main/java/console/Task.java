package console;

import model.components.Vertex;
import model.graph.DirectedGraph;
import model.graph.Graph;

import java.util.*;

public class Task {
    private static final String FOLDER = System.getProperty("user.dir") + "/src/main/resources/";

    private static Graph graph;

    // Ia (1)
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

    // Ia (2)
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

    // II (4)
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

    // II (5)

}
