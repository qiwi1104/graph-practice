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

public abstract class Graph {
    public static class IO {
        private static JSONArray readFile(String path) {
            StringBuilder stringBuilder = new StringBuilder("");

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

        public static <T> void saveToFile(String path, Graph graph) {
            try {
                FileWriter fw = new FileWriter(new File(path));

                fw.write(graph.toJSON());

                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected Map<Vertex, List<Vertex>> adjacencyList;
    protected List<Edge> edgeList;

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

                edgeList.forEach((e) -> {
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

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Vertex vertex = new Vertex(jsonObject.get("label").toString());

            List<Vertex> vertices = new ArrayList<>();
            for (int j = 0; j < jsonObject.getJSONArray("vertices").length(); j++) {
                Vertex key = new Vertex(jsonObject.getJSONArray("vertices").get(j).toString());
                vertices.add(key);
            }
            adjacencyList.putIfAbsent(vertex, vertices);

            if (jsonObject.has("edges")) {
                JSONArray jsonEdges = jsonObject.getJSONArray("edges");

                for (int j = 0; j < jsonEdges.length(); j++) {
                    edgeList.add(new Edge(
                            vertex.getLabel(),
                            jsonEdges.getJSONObject(j).get("to").toString(),
                            Integer.parseInt(jsonEdges
                                    .getJSONObject(j)
                                    .get("weight")
                                    .toString())));
                }
            }
        }
    }

    protected boolean has(Vertex searchIn, Vertex vertex) {
        for (Vertex vertexIt : adjacencyList.get(searchIn)) {
            if (vertexIt.equals(vertex)) {
                return true;
            }
        }

        return false;
    }

    public void addVertex(String label) {
        adjacencyList.putIfAbsent(new Vertex(label), new ArrayList<>());
    }

    public void addEdge(String from, String to) {
        Vertex vertex1 = new Vertex(from);
        Vertex vertex2 = new Vertex(to);

        if (!has(vertex1, vertex2)) {
            adjacencyList.get(vertex1).add(vertex2);
        }

        if (!has(vertex2, vertex1)) {
            adjacencyList.get(vertex2).add(vertex1);
        }
    }

    public void removeVertex(String label) {
        Vertex vertex = new Vertex(label);
        adjacencyList.values().forEach(e -> e.remove(vertex));
        adjacencyList.remove(vertex);
    }

    public void removeEdge(String from, String to) {
        Vertex vertex1 = new Vertex(from);
        Vertex vertex2 = new Vertex(to);

        List<Vertex> adjacentVertices1 = adjacencyList.get(vertex1);
        List<Vertex> adjacentVertices2 = adjacencyList.get(vertex2);

        if (adjacentVertices1 != null)
            adjacentVertices1.remove(vertex2);
        if (adjacentVertices2 != null)
            adjacentVertices2.remove(vertex1);
    }

    public List<Vertex> getAdjacentVertices(String label) {
        return adjacencyList.get(new Vertex(label));
    }

    public Map<Vertex, List<Vertex>> getAdjacencyList() {
        return new HashMap<>(adjacencyList);
    }

    public void print() {
        for (Map.Entry<Vertex, List<Vertex>> entry : getAdjacencyList().entrySet()) {
            System.out.print(entry.getKey().getLabel() + ": ");
            entry.getValue().forEach(v -> System.out.print(v.getLabel() + " "));
            System.out.println();
        }
    }

    /*
     * Deep copy
     * */
    protected Map<Vertex, List<Vertex>> cloneList() {
        Map<Vertex, List<Vertex>> copyList = new HashMap<>();

        for (Map.Entry<Vertex, List<Vertex>> entry : getAdjacencyList().entrySet()) {
            List<Vertex> adjacentVertices = new ArrayList<>();

            for (Vertex adjacentVertex : entry.getValue()) {
                adjacentVertices.add(adjacentVertex.clone());
            }

            copyList.put(entry.getKey().clone(), adjacentVertices);
        }

        return copyList;
    }

    @Override
    public Graph clone() {
        Graph copy = null;

        try {
            copy = (Graph) super.clone();
            copy.adjacencyList = cloneList();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copy;
    }
}
