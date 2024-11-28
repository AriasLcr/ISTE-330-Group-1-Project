import controller.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting application...");
        System.out.println("Loading data...");
        Controller controller = new Controller();
        controller.start();
    }
}
