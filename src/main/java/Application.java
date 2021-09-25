import console.UI;
import model.graph.DirectedGraph;
import model.graph.Graph;
import model.graph.UndirectedGraph;

public class Application {
    private static final String FOLDER = System.getProperty("user.dir") + "/src/main/resources/";

    private static void run() {
        UI.run();
    }

    public static void main(String[] args) {
        Application.run();
    }
}
