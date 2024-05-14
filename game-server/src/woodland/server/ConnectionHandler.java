package woodland.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Handles incoming socket connections for woodland's game server. This class processes
 * client HTTP requests and sends appropriate responses following the HTTP protocol in the
 * application/json format.
 */
public class ConnectionHandler extends Thread {

    // HTTP response codes.
    private static final int OK = 200;
    private static final int NOT_FOUND = 404;
    private static final int METHOD_NOT_ALLOWED = 405;

    // Client connection socket.
    private Socket socket;

    // Reader and writer for client input and output.
    private BufferedReader in;
    private PrintWriter out;

    // Handles the state and logic of the game through Json.
    private GameStateManager gameStateManager;

    /**
     * Creates a connection handler with a client socket and a game seed and
     * sets up the input and output streams for client communication.
     *
     * @param socket The client socket.
     * @param seed The seed used for game randomisation.
     */
    public ConnectionHandler(Socket socket, long seed) {
        this.socket = socket;
        this.gameStateManager = new GameStateManager(seed);
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            cleanup();
        }
    }

    /**
     * Continuously listens for client requests and processes them until an
     * exception occurs or the client disconnects. This method is the entry
     * point for the thread after start() is called.
     */
    public void run() {
        try {
            while (true) {
                handleRequests();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            cleanup();
        }
    }

    /**
     * Accepts clients HTTP requests by dispatching the request based on the
     * HTTP method and path specified. Can handle GET, POST and OPTIONS requests.
     *
     * @throws IOException If an I/O error occurs while reading the request.
     */
    public void handleRequests() throws IOException {
        String line = in.readLine();
        if (line == null) {
            throw new IOException("Client " + socket.getInetAddress() + " disconnected.");
        }
        // Read HTTP method and path from initial request.
        String method = line.split(" ")[0];
        String path = line.split(" ")[1];

        // Accept request based on HTTPS method
        switch (method) {
            case "GET":
                handleGetRequest(path);
                break;
            case "POST":
                handlePostRequest(path);
                break;
            case "OPTIONS":
                handleOptionsRequest();
                break;
            default:
                sendResponse(METHOD_NOT_ALLOWED, "Method Not Allowed", null);
        }

        // Discard the rest of the headers.
        while (in.ready()) {
            line = in.readLine();
        }
    }

    /**
     * Sends a HTTP response to the client with the specified status code, status message,
     * and response body. It constructs the HTTP response headers with included CORS headers
     * to allow for cross-origin requests.
     *
     * @param statusCode The HTTP status code to be sent.
     * @param statusMessage The HTTP status message to be sent.
     * @param body The response body as a JSON string, or null if no body is to be sent.
     */
    private void sendResponse(int statusCode, String statusMessage, String body) {
        out.print("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n");

        // Add CORS headers.
        out.print("Access-Control-Allow-Origin: *\r\n");
        out.print("Access-Control-Allow-Methods: *\r\n");
        out.print("Access-Control-Allow-Headers: *\r\n");
        out.print("Access-Control-Max-Age: 86400\r\n");

        // Include response body if not null.
        if (body != null) {
            out.print("Content-Type: application/json\r\n");
            out.print("Content-Length: " + body.length() + "\r\n");
            // Separate headers from body.
            out.print("\r\n" + body);
        } else {
            out.print("\r\n");
        }
        // Flush the stream to send off response correctly.
        out.flush();
    }

    /**
     * Handles POST requests by reading and parsing the request body, then dispatching
     * the request based on the specified path. Supports game actions and game reset requests.
     * Sends a 404 response if an incorrect path is specified.
     *
     * @param path HTTP request path.
     * @throws IOException If an I/O error occurs when reading the request body.
     */
    private void handlePostRequest(String path) throws IOException {
        // Read and parse content length.
        String line;
        int contentLength = 0;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        // Read and parse the request body.
        char[] chars = new char[contentLength];
        in.read(chars, 0, contentLength);
        String requestBody = new String(chars);

        // Handle request based on path.
        if (path.equals("/game") && requestBody != null) {
            JsonObject requestJSON = Json.createReader(new StringReader(requestBody)).readObject();
            sendResponse(OK, "OK", gameStateManager.handleAction(requestJSON));
        } else if (path.equals("/reset")) {
            sendResponse(OK, "OK", gameStateManager.reset());
        } else {
            sendResponse(NOT_FOUND, "Not Found", null);
        }
    }

    /**
     * Handles GET requests based on the specified path. It supports requests for
     * the server root and game information. Sends a 404 response if an incorrect
     * path is specified.
     *
     * @param path HTTP request path.
     */
    private void handleGetRequest(String path) {
        if (path.equals("/")) {
            // Respond with a basic status message for the server root.
            JsonObject statusJSON = Json.createObjectBuilder().add("status", "ok").build();
            sendResponse(OK, "OK", statusJSON.toString());
        } else if (path.equals("/game")) {
            sendResponse(OK, "OK", gameStateManager.gameState());
        } else {
            sendResponse(NOT_FOUND, "Not Found", null);
        }
    }

    /**
     * Handles OPTIONS requests used in preflight requests in CORS protocol by
     * sending back a standard response including headers that allow cross-
     * origin requests.
     */
    private void handleOptionsRequest() {
        sendResponse(OK, "OK", null);
    }

    /**
     * Closes the input and output streams and the client connection to clean up
     * resources when a client disconnects or an error occurrs.
     */
    private void cleanup() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
