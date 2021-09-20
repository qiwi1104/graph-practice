package model.graph;

import java.util.HashMap;

public class UndirectedGraph extends Graph implements Cloneable {
    public UndirectedGraph() {
        adjacencyList = new HashMap<>();
    }

    public UndirectedGraph(Graph graph) {
        this.adjacencyList = graph.cloneList();
    }

    public UndirectedGraph(String path) {
        newGraphFromJSON(path);
    }

    @Override
    public UndirectedGraph clone() {
        return (UndirectedGraph) super.clone();
    }
}
