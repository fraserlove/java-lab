import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A Server class that deals with client requests by opening a socket for every client and creating a connectionHandler
 * object to deal with each client. The class has also maintains a list of all the clients connected to the server for
 * further use.
 */
public class Server extends Thread {

    private ServerSocket srvSocket;
    private int port;

    private ArrayList<String> clients = new ArrayList<String>();
    private boolean serverRunning = false;

    public Server(int port) { this.port = port; }

    public void removeClient(String client) { clients.remove(client); }

    public ArrayList<String> getClients() { return clients; }

    public void close() { serverRunning = false; }

    /**
     * Constantly listens for new client requests and creates a new connectionHandler for each one.
     */
    public void run() {

        try {
            srvSocket = new ServerSocket(port);
            serverRunning = true;
            while (serverRunning) {
                Socket socket = srvSocket.accept(); // waits until client requests a connection
                clients.add(socket.getInetAddress().getHostAddress());
                ConnectionHandler connectionHandler = new ConnectionHandler(socket);
                connectionHandler.start();
            }
        }
        catch (IOException ioException) {
            System.out.println("Server error when trying to connect to client - " + ioException.getMessage());
        }
    }

}
