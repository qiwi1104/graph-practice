package model.graph;

import model.components.Edge;
import model.components.Vertex;

import java.util.ArrayList;
import java.util.HashMap;

import static model.graph.Graph.Weight.*;

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

    private void addEdge(String from, String to, Weight weight) {
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
    public void addEdge(String from, String to) {
        if (!weight.equals(WEIGHTED)) {
            if (weight.equals(UNKNOWN)) {
                addEdge(from, to, UNKNOWN);
                weight = NOT_WEIGHTED;
            } else if (weight.equals(NOT_WEIGHTED)) {
                addEdge(from, to, NOT_WEIGHTED);
            }
        } else {
            System.out.println("Specify weight (this graph is weighted)");
        }
    }

    @Override
    public void addEdge(String from, String to, int weight) {
        if (!super.weight.equals(NOT_WEIGHTED)) {
            if (super.weight.equals(UNKNOWN)) {
                super.weight = WEIGHTED;
            }

            addEdge(from, to, WEIGHTED);

            Vertex fromVertex = getVertex(from);
            Vertex toVertex = getVertex(to);

            if (pathExists(fromVertex, toVertex)) {
                Edge edge = new Edge(fromVertex, toVertex, weight);

                if (edgeList.stream().noneMatch(e -> e.equals(edge))) {
                    edgeList.add(edge);
                }
            }
        } else {
            System.out.println("This graph is not weighted");
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
