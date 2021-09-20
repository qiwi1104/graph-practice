package model.graph;

import model.components.Vertex;

import java.util.*;

public class DirectedGraph extends Graph implements Cloneable {
    public DirectedGraph() {
        adjacencyList = new HashMap<>();
    }

    public DirectedGraph(Graph graph) {
        this.adjacencyList = graph.cloneList();
    }

    public DirectedGraph(String path) {
        newGraphFromJSON(path);
    }

    @Override
    public void addEdge(String from, String to) {
        Vertex vertex1 = new Vertex(from);
        Vertex vertex2 = new Vertex(to);

        if (!has(vertex1, vertex2)) {
            adjacencyList.get(vertex1).add(vertex2);
        }
    }

    @Override
    public void removeEdge(String from, String to) {
        Vertex vertex1 = new Vertex(from);
        Vertex vertex2 = new Vertex(to);

        if (adjacencyList.get(vertex1) != null) {
            adjacencyList.remove(vertex2);
        }
    }

    @Override
    public DirectedGraph clone() {
        return (DirectedGraph) super.clone();
    }
}
