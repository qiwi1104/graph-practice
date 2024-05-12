package console;

import model.components.Vertex;
import model.graph.DirectedGraph;
import model.graph.Graph;

import java.util.*;
import java.util.stream.Collectors;

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
}
