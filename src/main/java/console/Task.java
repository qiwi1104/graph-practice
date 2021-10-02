package console;

import model.components.Vertex;
import model.graph.DirectedGraph;
import model.graph.Graph;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        }
        else {
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
}
