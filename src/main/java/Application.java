import model.graph.DirectedGraph;
import model.graph.Graph;

public class Application {
    private static final String FOLDER = System.getProperty("user.dir") + "/src/main/resources/";

    private static void run() {
        Graph graph = new DirectedGraph(FOLDER + "input.json");

        graph.print();
    }

    public static void main(String[] args) {
        Application.run();
    }
}
