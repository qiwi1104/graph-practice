import console.UI;

public class Application {
    public static final String FOLDER = System.getProperty("user.dir") + "/src/main/resources/";

    private static void run() {
        UI.run();
    }

    public static void main(String[] args) {
        Application.run();
    }
}
