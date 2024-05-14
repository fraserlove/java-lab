package vectordrawing.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * A client for communicating with the drawing server.
 */
public class Client {

    // Server connection.
    private Socket socket;

    // Reader and writer for server input and output.
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Creates a drawing client to set up a connection with the server at the specified
     * port and address and performs an initial login request with the server.
     *
     * @param host Address of the server.
     * @param port Port of the server.
     * @throws IOException If there is a communication error with the server.
     */
    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.login("a4033e72-5403-4a0c-b0ae-3d8bf335dd92");
    }

    /**
     * Sends a JSON request to the server to log in.
     * 
     * @param tokenData Login token.
     * @throws IOException If there is a communication error with the server.
     */
    public void login(String tokenData) throws IOException {
        JsonObject loginJson = Json.createObjectBuilder()
                .add("action", "login")
                .add("data", Json.createObjectBuilder().add("token", tokenData))
                .build();
        sendJson(loginJson);
    }

    /**
     * Sends a JSON request to the server to get all shapes.
     * 
     * @return Shapes as a JSON array.
     * @throws IOException If there is a communication error with the server.
     */
    public JsonArray getShapes() throws IOException {
        JsonObject json = Json.createObjectBuilder()
                .add("action", "getDrawings")
                .build();
        return (JsonArray) sendJson(json);
    }

    /**
     * Sends a JSON request to the server to add a shape.
     * 
     * @param json JSON object containing the shape data.
     * @return ID of the shape.
     * @throws IOException If there is a communication error with the server.
     */
    public String addShape(JsonObject json) throws IOException {
        JsonObject addShapeJson = Json.createObjectBuilder()
            .add("action", "addDrawing")
            .add("data", json)
            .build();
        return ((JsonObject) sendJson(addShapeJson)).getString("id");
    }

    /**
     * Sends a JSON request to the server to update a shape with the specified ID.
     * 
     * @param shape JSON object containing the shape data.
     * @return Success of the update.
     */
    public boolean updateShape(JsonObject shape) throws IOException {
        JsonObject updateShapeJson = Json.createObjectBuilder()
                .add("action", "updateDrawing")
                .add("data", Json.createObjectBuilder()
                    .add("id", shape.getString("id"))
                    .add("x", shape.getInt("x"))
                    .add("y", shape.getInt("x"))
                    .add("properties", shape.getJsonObject("properties")))
                .build();
        return sendJson(updateShapeJson) != null;
    }

    /**
     * Sends a JSON request to the server to delete a shape with the specified ID.
     * 
     * @param id ID of the shape to delete.
     * @throws IOException If there is a communication error with the server.
     */
    public void deleteShape(String id) throws IOException {
        JsonObject deleteShapeJson = Json.createObjectBuilder()
                .add("action", "deleteDrawing")
                .add("data", Json.createObjectBuilder()
                    .add("id", id))
                .build();
        sendJson(deleteShapeJson);
    }

    /**
     * Sends a JSON object to the server and receives a JSON status response. If a non-empty
     * response is received, it is parsed into a JSON object. If the response indicates an "ok"
     * result, the method extracts the "id" from the "data" part of the JSON response and
     * returns it. This method returns null if the response is empty or there is no "id" in
     * the "data" part of the JSON response.
     * 
     * @param json JSON object to send to the server.
     * @return JSON value containing the response from the server.
     * @throws IOException If there is a communication error with the server.
     */
    private JsonValue sendJson(JsonObject json) throws IOException {
        this.out.println(json.toString());
        String response = in.readLine();

        if (response != null && !response.isEmpty()) {
            JsonValue jsonValue = Json.createReader(new StringReader(response)).read();
            // Return the JSON object containing the objects ID if it exists in the response JSON.
            if (jsonValue.getValueType() == JsonValue.ValueType.OBJECT) {
                JsonObject jsonObject = ((JsonObject) jsonValue);
                if (jsonObject.getString("result").equals("ok")) {
                    JsonObject dataObject = jsonObject.getJsonObject("data");
                    if (dataObject != null) {
                        return dataObject;
                    }
                }
                // Throw an exception if the response JSON contains an error.
                if (jsonObject.getString("result").equals("error")) {
                    throw new IOException(jsonObject.getString("message"));
                }
            }
             // Return the JSON object containing the objects ID if it exists in the response JSON.
            if (jsonValue.getValueType() == JsonValue.ValueType.ARRAY) {
                    return (JsonArray) jsonValue;
            }
        }
        return null;
    }

    /**
     * Closes the connection with the server.
     * 
     * @throws IOException If there is a communication error with the server.
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
