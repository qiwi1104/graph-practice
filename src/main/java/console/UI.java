package console;

import model.components.Vertex;
import model.graph.DirectedGraph;
import model.graph.Graph;
import model.graph.UndirectedGraph;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

            if (str.matches("new directed graph from file [a-zA-Z0-9]+.json")) {
                graph = new DirectedGraph(FOLDER + str.split(" ")[5]);
                System.out.println("A directed graph has been created");
            }

            if (str.matches("new undirected graph from file [a-zA-Z0-9]+.json")) {
                graph = new UndirectedGraph(FOLDER + str.split(" ")[5]);
                System.out.println("An undirected graph has been created");
            }

            if (str.matches("add vertex [a-zA-Z0-9]+")) {
                previousState = graph.getAdjacencyList();

                graph.addVertex(str.split(" ")[2]);

                if (!previousState.equals(graph.getAdjacencyList())) {
                    System.out.println("Vertex " + str.split(" ")[2] + " has been added");
                }
            }

            if (str.matches("add edge [a-zA-Z0-9]+ [a-zA-Z0-9]+")) {
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

            if (str.matches("add edge [a-zA-Z0-9]+ [a-zA-Z0-9]+ [0-9]+")) {
                previousState = graph.getAdjacencyList();

                if (!previousState.equals(graph.getAdjacencyList())) {
                    graph.addEdge(str.split(" ")[2], str.split(" ")[3], Integer.parseInt(str.split(" ")[4]));

                    System.out.print("Edge " + str.split(" ")[2]);

                    if (graph instanceof DirectedGraph) {
                        System.out.print(" -> ");
                    } else if (graph instanceof UndirectedGraph) {
                        System.out.print(" <-> ");
                    }

                    System.out.println(str.split(" ")[3] + " has been added");
                }
            }

            if (str.matches("remove vertex [a-zA-Z0-9]+")) {
                previousState = graph.getAdjacencyList();

                graph.removeVertex(str.split(" ")[2]);

                if (!previousState.equals(graph.getAdjacencyList())) {
                    System.out.println("Vertex " + str.split(" ")[2] + " has been removed");
                }
            }

            if (str.matches("remove edge [a-zA-Z0-9]+ [a-zA-Z0-9]+")) {
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

            if (str.matches("print")) {
                graph.print();
            }
        }
    }
}