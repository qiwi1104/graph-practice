package model.graph;

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
        Vertex fromVertex = new Vertex(from);
        Vertex toVertex = new Vertex(to);

        if (!isAbsent(fromVertex) && !isAbsent(toVertex)) {
            if (!has(fromVertex, toVertex)) {
                adjacencyList.get(fromVertex).add(toVertex);
            }
        } else {
            if (isAbsent(fromVertex)) System.out.println("Vertex " + fromVertex.getLabel() + " doesn't exist");
            if (isAbsent(toVertex)) System.out.println("Vertex " + toVertex.getLabel() + " doesn't exist");
        }
    }

    @Override
    public void removeEdge(String from, String to) {
        Vertex fromVertex = new Vertex(from);
        Vertex toVertex = new Vertex(to);

        if (adjacencyList.get(fromVertex) != null) {
            adjacencyList.get(fromVertex).remove(toVertex);
        } else {
            System.out.println("Vertex " + fromVertex.getLabel() + " doesn't exist");
            if (isAbsent(toVertex)) System.out.println("Vertex " + toVertex.getLabel() + " doesn't exist");
        }
    }

    @Override
    public DirectedGraph clone() {
        return (DirectedGraph) super.clone();
    }
}
