import model.components.Vertex;
import model.graph.DirectedGraph;
import model.graph.Graph;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Task {
    private static Graph graph;

    // Ia (1)
    public static void isolatedVertices() {
        graph = new DirectedGraph(Application.FOLDER + "input4.json");

        Set<Vertex> isolatedVertices = new HashSet<>(graph.getAdjacencyList().keySet());

        /*
         * Searches for isolated vertices
         * */
        for (Map.Entry<Vertex, List<Vertex>> entry : graph.getAdjacencyList().entrySet()) {
            for (Vertex from : graph.getAdjacencyList().keySet()) {
                for (Vertex to : entry.getValue()) {
                    if (to.getLabel().equals(from.getLabel())) {
                        isolatedVertices.remove(from);
                    }
                }
            }
        }

        System.out.println("Isolated vertices:");
        isolatedVertices.forEach(v -> System.out.println(v.getLabel()));
    }

    // Ia (2)
    public static void adjacentVertices(String label) {
        graph = new DirectedGraph(Application.FOLDER + "input2.json");
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
