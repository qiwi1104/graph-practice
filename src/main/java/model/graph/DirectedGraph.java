package model.graph;

import model.components.Edge;
import model.components.Vertex;

import java.util.ArrayList;
import java.util.HashMap;

public class DirectedGraph extends Graph implements Cloneable {
    public DirectedGraph() {
        adjacencyList = new HashMap<>();
        edgeList = new ArrayList<>();
    }

    public DirectedGraph(Graph graph) {
        this.adjacencyList = graph.cloneAdjacencyList();
        this.edgeList = graph.cloneEdgeList();
    }

    public DirectedGraph(String path) {
        newGraphFromJSON(path);
    }

    @Override
    public void addEdge(String from, String to) {
        Vertex fromVertex = getVertex(from);
        Vertex toVertex = getVertex(to);

        if (fromVertex != null && toVertex != null) {
            if (pathExists(fromVertex, toVertex)) {
                System.out.println("Edge " + from + " -> " + to + " already exists");
            } else {
                adjacencyList.get(fromVertex).add(toVertex);
            }
        } else {
            if (fromVertex == null) System.out.println("Vertex " + from + " doesn't exist");
            if (toVertex == null) System.out.println("Vertex " + to + " doesn't exist");
        }
    }

    @Override
    public void addEdge(String from, String to, int weight) {
        this.addEdge(from, to);

        Vertex fromVertex = getVertex(from);
        Vertex toVertex = getVertex(to);

        if (fromVertex != null && toVertex != null) {
            if (pathExists(fromVertex, toVertex)) {
                System.out.println("Edge " + from + " -> " + to + " already exists");
            } else {
                adjacencyList.get(fromVertex).add(toVertex);
                edgeList.add(new Edge(fromVertex, toVertex, weight));
            }
        } else {
            if (fromVertex == null) System.out.println("Vertex " + from + " doesn't exist");
            if (toVertex == null) System.out.println("Vertex " + to + " doesn't exist");
        }
    }

    @Override
    public void removeEdge(String from, String to) {
        Vertex fromVertex = getVertex(from);
        Vertex toVertex = getVertex(to);

        if (!pathExists(fromVertex, toVertex)) {
            System.out.println("Edge " + from + " -> " + to + " doesn't exist");
        } else {
            if (fromVertex != null) {
                adjacencyList.get(fromVertex).remove(toVertex);
            } else {
                System.out.println("Vertex " + from + " doesn't exist");
            }
            if (toVertex == null) System.out.println("Vertex " + to + " doesn't exist");
        }
    }

    @Override
    public DirectedGraph clone() {
        return (DirectedGraph) super.clone();
    }
}
