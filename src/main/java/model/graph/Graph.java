package model.graph;

import model.components.Edge;
import model.components.Vertex;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static model.graph.Graph.Weight.*;
import static model.graph.Graph.Weight.NOT_WEIGHTED;

public abstract class Graph {
    public static class IO {
        private static JSONArray readFile(String path) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                Scanner scanner = new Scanner(new File(path));

                while (scanner.hasNextLine()) {
                    stringBuilder.append(scanner.nextLine());
                }

                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return new JSONArray(stringBuilder.toString());
        }

        public static void saveToFile(String path, Graph graph) {
            try {
                FileWriter fw = new FileWriter(new File(path));

                fw.write(graph.toJSON());

                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected enum Weight {
        WEIGHTED, NOT_WEIGHTED, UNKNOWN
    }

    protected Map<Vertex, List<Vertex>> adjacencyList;
    protected List<Edge> edgeList; // used with weighted graphs
    protected Weight weight = Weight.UNKNOWN;

    private String toJSON() {
        JSONArray jsonGraph = new JSONArray();

        for (Map.Entry<Vertex, List<Vertex>> entry : getAdjacencyList().entrySet()) {
            JSONObject jsonVertex = new JSONObject();
            jsonVertex.put("label", entry.getKey().getLabel());

            JSONArray jsonVertices = new JSONArray();
            entry.getValue().forEach(v -> jsonVertices.put(v.getLabel()));

            jsonVertex.put("vertices", jsonVertices);
            jsonGraph.put(jsonVertex);

            if (edgeList != null) {
                JSONArray jsonEdges = new JSONArray();

                edgeList.forEach(e -> {
                    if (e.getFrom().equals(entry.getKey())) {
                        JSONObject jsonEdge = new JSONObject();

                        jsonEdge.put("to", e.getTo().getLabel());
                        jsonEdge.put("weight", String.valueOf(e.getWeight()));
                        jsonEdges.put(jsonEdge);

                        jsonVertex.put("edges", jsonEdges);
                    }
                });
            }
        }

        return jsonGraph.toString();
    }

    protected void newGraphFromJSON(String path) {
        JSONArray jsonArray = IO.readFile(path);
        adjacencyList = new HashMap<>();
        edgeList = new ArrayList<>();
        weight = Weight.NOT_WEIGHTED;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Vertex vertex = new Vertex(jsonObject.get("label").toString());

            /*
             * Checks if Vertex exists as a value, but not as a key yet
             * */
            for (Map.Entry<Vertex, List<Vertex>> entry : getAdjacencyList().entrySet()) {
                for (Vertex v : entry.getValue()) {
                    if (v.getLabel().equals(vertex.getLabel())) {
                        vertex = v;
                        break;
                    }
                }
            }

            Vertex temp = getVertex(vertex.getLabel());
            List<Vertex> vertices = temp != null ? adjacencyList.get(temp) : new ArrayList<>();

            for (int j = 0; j < jsonObject.getJSONArray("vertices").length(); j++) {
                Vertex key = getVertex(jsonObject.getJSONArray("vertices").get(j).toString());

                if (key == null) {
                    key = new Vertex(jsonObject.getJSONArray("vertices").get(j).toString());
                }

                vertices.add(key);
            }

            if (!adjacencyList.containsKey(vertex)) {
                adjacencyList.put(vertex, vertices);
            }

            if (jsonObject.has("edges")) { // weighted graph
                weight = Weight.WEIGHTED;
                JSONArray jsonEdges = jsonObject.getJSONArray("edges");

                for (int j = 0; j < jsonEdges.length(); j++) {
                    Vertex to = getVertex(jsonEdges.getJSONObject(j).get("to").toString());
                    if (to == null) to = new Vertex(jsonEdges.getJSONObject(j).get("to").toString());

                    /*
                     * Checks if Vertex exists as a value, but not as a key yet
                     * */
                    for (Map.Entry<Vertex, List<Vertex>> entry : getAdjacencyList().entrySet()) {
                        for (Vertex v : entry.getValue()) {
                            if (v.getLabel().equals(to.getLabel())) {
                                to = v;
                                break;
                            }
                        }
                    }

                    edgeList.add(new Edge(vertex, to,
                            Integer.parseInt(jsonEdges
                                    .getJSONObject(j)
                                    .get("weight")
                                    .toString())));
                }
            }
        }
    }

    protected boolean pathExists(Vertex vertex1, Vertex vertex2) {
        if (vertex1 != null && vertex2 != null) {
            for (Vertex vertexIt : adjacencyList.get(vertex1)) {
                if (vertexIt.equals(vertex2)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected Vertex getVertex(String label) {
        for (Vertex vertex : adjacencyList.keySet()) {
            if (vertex.getLabel().equals(label)) {
                return vertex;
            }
        }

        return null;
    }

    public void addVertex(String label) {
        Vertex vertex = getVertex(label);

        if (vertex == null) {
            adjacencyList.put(new Vertex(label), new ArrayList<>());
        } else {
            System.out.println("Vertex " + label + " already exists");
        }
    }

    private void addEdge(String from, String to, Weight weight) {
        Vertex fromVertex = getVertex(from);
        Vertex toVertex = getVertex(to);

        if (fromVertex != null && toVertex != null) {
            if (pathExists(fromVertex, toVertex)) {
                System.out.println("Edge " + from + " <-> " + to + " already exists");
            } else {
                if (!pathExists(fromVertex, toVertex)) {
                    adjacencyList.get(fromVertex).add(toVertex);
                }

                if (!pathExists(toVertex, fromVertex)) {
                    adjacencyList.get(toVertex).add(fromVertex);
                }
            }
        } else {
            if (fromVertex == null) System.out.println("Vertex " + from + " doesn't exist");
            if (toVertex == null) System.out.println("Vertex " + to + " doesn't exist");
        }
    }

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

    public void addEdge(String from, String to, int weight) {
        if (!this.weight.equals(NOT_WEIGHTED)) {
            if (this.weight.equals(UNKNOWN)) {
                this.weight = WEIGHTED;
            }

            addEdge(from, to, WEIGHTED);

            Vertex fromVertex = getVertex(from);
            Vertex toVertex = getVertex(to);

            if (pathExists(fromVertex, toVertex)) {
                Edge edge = new Edge(fromVertex, toVertex, weight);

                if (edgeList.stream().noneMatch(e -> e.equals(edge))) {
                    edgeList.add(edge);
                    edgeList.add(new Edge(toVertex, fromVertex, weight));
                }
            }
        } else {
            System.out.println("This graph is not weighted");
        }
    }

    public void removeVertex(String label) {
        Vertex vertex = getVertex(label);

        if (vertex != null) {
            adjacencyList.values().forEach(e -> e.remove(vertex));
            adjacencyList.remove(vertex);
        } else {
            System.out.println("Vertex " + label + " doesn't exist");
        }
    }

    public void removeEdge(String from, String to) {
        Vertex fromVertex = getVertex(from);
        Vertex toVertex = getVertex(to);

        List<Vertex> adjacentVerticesFrom = adjacencyList.get(fromVertex);
        List<Vertex> adjacentVerticesTo = adjacencyList.get(toVertex);

        if (!pathExists(fromVertex, toVertex)) {
            System.out.println("Edge " + from + " <-> " + to + " doesn't exist");
        } else {
            if (adjacentVerticesFrom != null) {
                adjacentVerticesFrom.remove(toVertex);
            } else {
                if (fromVertex == null) System.out.println("Vertex " + from + " doesn't exist");
            }

            if (adjacentVerticesTo != null) {
                adjacentVerticesTo.remove(fromVertex);
            } else {
                if (toVertex == null) System.out.println("Vertex " + to + " doesn't exist");
            }
        }
    }

    public List<Vertex> getReachableVertices(String label) {
        Vertex vertex = getVertex(label);

        if (vertex != null) {
            return adjacencyList.get(vertex);
        } else {
            System.out.println("Vertex " + label + " doesn't exist");
            return new ArrayList<>();
        }
    }

    public Map<Vertex, List<Vertex>> getAdjacencyList() {
        return cloneAdjacencyList();
    }

    public void setAdjacencyList(Map<Vertex, List<Vertex>> adjacencyList) {
        Map<Vertex, List<Vertex>> copyList = new HashMap<>();

        for (Map.Entry<Vertex, List<Vertex>> entry : adjacencyList.entrySet()) {
            List<Vertex> adjacentVertices = new ArrayList<>();

            for (Vertex adjacentVertex : entry.getValue()) {
                adjacentVertices.add(adjacentVertex.clone());
            }

            copyList.put(entry.getKey().clone(), adjacentVertices);
        }

        this.adjacencyList = copyList;
    }

    public void print() {
        for (Map.Entry<Vertex, List<Vertex>> entry : getAdjacencyList().entrySet()) {
            System.out.print(entry.getKey().getLabel() + ": ");

            entry.getValue().forEach(v -> {
                System.out.print(v.getLabel());

                if (!edgeList.isEmpty()) {
                    for (Edge edge : edgeList) {
                        if (edge.getFrom().equals(entry.getKey()) && edge.getTo().equals(v)) {
                            System.out.print("(" + edge.getWeight() + ") ");
                        }
                    }
                } else {
                    System.out.print(" ");
                }
            });
            System.out.println();
        }
    }

    /*
     * Deep copy
     * Used in constructors & when returning adjacency list
     * */
    protected Map<Vertex, List<Vertex>> cloneAdjacencyList() {
        Map<Vertex, List<Vertex>> copyList = new HashMap<>();

        for (Map.Entry<Vertex, List<Vertex>> entry : adjacencyList.entrySet()) {
            List<Vertex> adjacentVertices = new ArrayList<>();

            for (Vertex adjacentVertex : entry.getValue()) {
                adjacentVertices.add(adjacentVertex.clone());
            }

            copyList.put(entry.getKey().clone(), adjacentVertices);
        }

        return copyList;
    }

    /*
     * Deep copy
     * Used in constructors & when returning edges list
     * */
    protected List<Edge> cloneEdgeList() {
        List<Edge> copyList = new ArrayList<>();

        edgeList.forEach(e -> copyList.add(e.clone()));

        return copyList;
    }

    @Override
    public Graph clone() {
        Graph copy = null;

        try {
            copy = (Graph) super.clone();
            copy.adjacencyList = cloneAdjacencyList();
            copy.edgeList = cloneEdgeList();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copy;
    }
}
