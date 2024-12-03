import controller.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Current Working Directory: " + Paths.get("").toAbsolutePath().toString());

        System.out.println("Starting application...");
        System.out.println("Loading data...");
        Controller controller = new Controller();
        controller.start();
    }
}
