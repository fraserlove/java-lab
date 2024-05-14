package woodland.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server for woodland diplomacy. Listens for client connections and
 * handles each connection in a separate thread.
 */
public class GameServer {

    /**
     * Creates a woodland game server that listens on the specified port.
     * A new connection handler is created to manage communication with each client.
     *
     * @param port The port on which the server should listen.
     * @param seed A seed for the game's random number generator.
     */
    public GameServer(int port, long seed) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port + ".");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress() + " connected.");
                // Creating and starting a new thread to deal with client.
                new ConnectionHandler(clientSocket, seed).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
