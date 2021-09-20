package model.graph;

import java.util.ArrayList;
import java.util.HashMap;

public class UndirectedGraph extends Graph implements Cloneable {
    public UndirectedGraph() {
        adjacencyList = new HashMap<>();
        edgeList = new ArrayList<>();
    }

    public UndirectedGraph(Graph graph) {
        this.adjacencyList = graph.cloneAdjacencyList();
        this.edgeList = graph.cloneEdgeList();
    }

    public UndirectedGraph(String path) {
        newGraphFromJSON(path);
    }

    @Override
    public UndirectedGraph clone() {
        return (UndirectedGraph) super.clone();
    }
}
