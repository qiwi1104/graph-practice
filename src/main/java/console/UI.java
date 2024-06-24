package console;

import model.components.Edge;
import model.components.Vertex;
import model.graph.DirectedGraph;
import model.graph.Graph;
import model.graph.UndirectedGraph;

import java.util.*;

public class UI {
    private static final String FOLDER = System.getProperty("user.dir") + "/src/main/resources/";
    private static Graph graph;
    private static Map<Vertex, List<Vertex>> previousState;

    public static void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type /help to see available commands\n");

        while (scanner.hasNext()) {
            String str = scanner.nextLine();

            if (str.matches("/help")) {
                System.out.println("new directed graph - creates a directed graph");
                System.out.println("new undirected graph - creates an undirected graph");
                System.out.println("new directed graph from file {filename.json} - creates a directed graph loading adjacency list from a file");
                System.out.println("new undirected graph from file {filename.json} - creates an undirected graph loading adjacency list from a file");
                System.out.println("add vertex {label} - adds a vertex");
                System.out.println("add edge {vertex1} {vertex2} - adds an edge between two vertices");
                System.out.println("add edge {vertex1} {vertex2} {weight} - adds an edge between two vertices in a weighted graph");
                System.out.println("remove vertex {label} - removes a vertex");
                System.out.println("remove edge {vertex1} {vertex2} - removes an edge between two vertices");
                System.out.println("print - prints the adjacency list\n");
            }

            if (str.matches("new directed graph")) {
                graph = new DirectedGraph();
                System.out.println("A directed graph has been created");
            }

            if (str.matches("new undirected graph")) {
                graph = new UndirectedGraph();
                System.out.println("An undirected graph has been created");
            }

            if (str.matches("new directed graph from file \\w+.json")) {
                graph = new DirectedGraph(FOLDER + str.split(" ")[5]);
                System.out.println("A directed graph has been created");
            }

            if (str.matches("new undirected graph from file \\w+.json")) {
                graph = new UndirectedGraph(FOLDER + str.split(" ")[5]);
                System.out.println("An undirected graph has been created");
            }

            if (str.matches("add vertex \\w+")) {
                previousState = graph.getAdjacencyList();

                graph.addVertex(str.split(" ")[2]);

                if (!previousState.equals(graph.getAdjacencyList())) {
                    System.out.println("Vertex " + str.split(" ")[2] + " has been added");
                }
            }

            if (str.matches("add edge \\w+ \\w+ (.?\\d+)")) {
                previousState = graph.getAdjacencyList();

                int weight = Integer.parseInt(str.split(" ")[4]);

                if (weight >= 0) {
                    graph.addEdge(str.split(" ")[2], str.split(" ")[3], weight);
                } else {
                    System.out.println("Weight must be positive");
                }

                if (!previousState.equals(graph.getAdjacencyList())) {
                    System.out.print("Edge " + str.split(" ")[2]);

                    if (graph instanceof DirectedGraph) {
                        System.out.print(" -> ");
                    } else if (graph instanceof UndirectedGraph) {
                        System.out.print(" <-> ");
                    }

                    System.out.println(str.split(" ")[3] + " has been added");
                }
            } else if (str.matches("add edge \\w+ \\w+")) {
                previousState = graph.getAdjacencyList();

                graph.addEdge(str.split(" ")[2], str.split(" ")[3]);

                if (!previousState.equals(graph.getAdjacencyList())) {
                    System.out.print("Edge " + str.split(" ")[2]);

                    if (graph instanceof DirectedGraph) {
                        System.out.print(" -> ");
                    } else if (graph instanceof UndirectedGraph) {
                        System.out.print(" <-> ");
                    }

                    System.out.println(str.split(" ")[3] + " has been added");
                }
            }

            if (str.matches("remove vertex \\w+")) {
                previousState = graph.getAdjacencyList();

                graph.removeVertex(str.split(" ")[2]);

                if (!previousState.equals(graph.getAdjacencyList())) {
                    System.out.println("Vertex " + str.split(" ")[2] + " has been removed");
                }
            }

            if (str.matches("remove edge \\w+ \\w+")) {
                previousState = graph.getAdjacencyList();

                graph.removeEdge(str.split(" ")[2], str.split(" ")[3]);

                if (!previousState.equals(graph.getAdjacencyList())) {
                    System.out.print("Edge " + str.split(" ")[2]);

                    if (graph instanceof DirectedGraph) {
                        System.out.print(" -> ");
                    } else if (graph instanceof UndirectedGraph) {
                        System.out.print(" <-> ");
                    }

                    System.out.println(str.split(" ")[3] + " has been removed");
                }
            }

            if (str.equals("Ia1")) {
                Task.isolatedVertices(graph);
            }

            if (str.matches("Ia2 \\w+")) {
                Task.adjacentVertices(graph, str.split(" ")[1]);
            }

            if (str.matches("Ib")) {
                graph = Task.graphComplement(graph);
            }

            if (str.equals("BFS")) {
                Task.isStronglyConnected(graph);
            }

            if (str.equals("DFS")) {
                List<Vertex> vertices = new ArrayList<>(graph.getAdjacencyList().keySet());

                System.out.println("u v: ");

                str = scanner.nextLine();

                Vertex u = new Vertex(str.split(" ")[0]);
                Vertex v = new Vertex((str.split(" ")[1]));

                if (!vertices.contains(u)) {
                    System.out.println("Vertex " + u.getLabel() + " doesn't exist.");
                }
                if (!vertices.contains(v)) {
                    System.out.println("Vertex " + v.getLabel() + " doesn't exist.");
                }

                if (vertices.contains(u) && vertices.contains(v)) {
                    List<Edge> edgesToRemove = new ArrayList<>();

                    System.out.println("Edges: ");

                    while (!str.equals("0.")) {
                        str = scanner.nextLine();

                        if (str.equals("0.")) {
                            break;
                        }

                        Edge edge = new Edge(str.split(" ")[0], str.split(" ")[1]);
                        Edge edgeMirror = null;

                        if (graph instanceof DirectedGraph) {
                            edgeMirror = new Edge(edge.getTo(), edge.getFrom());
                        }

                        if (!graph.getAdjacencyList().get(edge.getFrom()).contains(edge.getTo())) {
                            System.out.println("Edge "
                                    + edge.getFrom().getLabel() + " -> "
                                    + edge.getTo().getLabel() + " doesn't exist");
                            break;
                        } else {
                            edgesToRemove.add(edge);

                            if (edgeMirror != null) {
                                edgesToRemove.add(edgeMirror);
                            }
                        }
                    }

                    Task.isReachable(graph, u, v, edgesToRemove);
                }
            }

            if (str.equals("MST")) {
                Task.MST(graph);
            }

            if (str.equals("IVa")) {
                List<Vertex> vertices = new ArrayList<>(graph.getAdjacencyList().keySet());

                System.out.println("start end L: ");

                str = scanner.nextLine();

                Vertex start = new Vertex(str.split(" ")[0]);
                Vertex end = new Vertex((str.split(" ")[1]));
                int l = Integer.parseInt(str.split(" ")[2]);

                if (!vertices.contains(start)) {
                    System.out.println("Vertex " + start.getLabel() + " doesn't exist.");
                }
                if (!vertices.contains(end)) {
                    System.out.println("Vertex " + end.getLabel() + " doesn't exist.");
                }

                if (vertices.contains(start) && vertices.contains(end)) {
                    boolean exists = Task.dijkstra(graph, start, end, l);

                    if (exists) {
                        System.out.println("Exists.");
                    } else {
                        System.out.println("Doesn't exist.");
                    }
                }
            }

            if (str.equals("IVb")) {
                List<Vertex> vertices = new ArrayList<>(graph.getAdjacencyList().keySet());

                System.out.println("u v1 v2: ");

                str = scanner.nextLine();

                Vertex u = new Vertex(str.split(" ")[0]);
                Vertex v1 = new Vertex((str.split(" ")[1]));
                Vertex v2 = new Vertex(str.split(" ")[2]);

                if (!vertices.contains(u)) {
                    System.out.println("Vertex " + u.getLabel() + " doesn't exist.");
                }
                if (!vertices.contains(v1)) {
                    System.out.println("Vertex " + v1.getLabel() + " doesn't exist.");
                }
                if (!vertices.contains(v2)) {
                    System.out.println("Vertex " + v2.getLabel() + " doesn't exist.");
                }

                if (vertices.contains(u) && vertices.contains(v1) && vertices.contains(v2)) {
//                    int[] distances = Task.bellmanFord(graph);

//                    System.out.println(
//                            Integer.parseInt(u.getLabel()) + " -> " + Integer.parseInt(v1.getLabel())
//                                    + " = " + distances[Integer.parseInt(v1.getLabel()) - 1] + "\n" +
//                                    Integer.parseInt(u.getLabel()) + " -> " + Integer.parseInt(v2.getLabel())
//                                    + " = " + distances[Integer.parseInt(v2.getLabel()) - 1]);
                }
            }

            if (str.equals("IVc")) {
                Task.bellmanFord(graph);
            }

            if (str.matches("print")) {
                if (graph != null) {
                    if (!graph.getAdjacencyList().isEmpty()) {
                        graph.print();
                    } else {
                        System.out.println("The graph has no vertices");
                    }
                } else {
                    System.out.println("The graph doesn't exist");
                }
            }
        }
    }
}
