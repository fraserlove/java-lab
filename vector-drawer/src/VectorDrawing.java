import vectordrawing.model.Model;
import vectordrawing.view.View;
import vectordrawing.controller.Controller;
import vectordrawing.networking.Client;

import java.io.IOException;

/**
 * Initiates the Vector Drawing GUI Application. Creates a model, controller and view as
 * part of the MVC design pattern then initiates a client to connect to the drawing server.
 */
public class VectorDrawing {

    private static final int WINDOW_WIDTH = 1080;
    private static final int WINDOW_HEIGHT = 720;

    private static final String HOST = "cs5001-p3.dynv6.net";
    private static final int PORT = 8080;

    /**
     * Initiates the Vector Drawing GUI Application.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        try {
            Client client = new Client(HOST, PORT);
            Model model = new Model();
            View view = new View(model, WINDOW_WIDTH, WINDOW_HEIGHT);
            new Controller(client, model, view);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
