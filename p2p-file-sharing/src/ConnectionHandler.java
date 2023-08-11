import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A ConnectionHandler class that is a threaded part of the server designed to deal with one specific connection.
 * The class opens a connection with the specified socket as soon as it is created. It can then detect if the client
 * has disconnected from the socket due to a nullPointerException thrown by buffered reader as it tries to read input
 * from the sockets input stream. If the client has disconnected the connectionHandler closes the connection on its
 * end and returns, exiting the thread cleanly.
 *
 * This class deals with server functionality that is needed for multiple connections. Therefore allowing the server
 * to deal with multiple requests at once.
 */
public class ConnectionHandler extends Thread {

    private Socket socket; // sends and receives data to and from the client

    private InputStream inputStream; // receives data from client through socket
    private OutputStream outputStream; // sends data to client through socket
    private BufferedWriter bufferedWriter; // allows data to be written to the output stream

    public ConnectionHandler(Socket socket) {

        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }
        catch (IOException ioException) {
            System.out.println("Connection handler error when setting up new connection - " + ioException.getMessage());
        }
    }

    /**
     * Constantly listens on the socket for a request and when then extracts a command and argument from the request.
     * If at any point a nullPointer is thrown the client has disconnected and the connection will be closed.
     */
    public void run() {

        String command = "";
        String arg = "";
        String request = "";

        while (true) {
            try {
                request = new BufferedReader(new InputStreamReader(inputStream)).readLine();
                command = request.replaceAll(" .*", "");
            }
            catch (IOException ioException) {
                System.out.println("ConnectionHandler error when trying to read request sent from client - " + ioException.getMessage());
            }
            catch (NullPointerException nullPointerException) {
                // If a line cannot be read from request then the client has disconnected from the socket a nullPointerException occurs
                // This is how we know a client has disconnected, therefore we will close the connectionHandler
                closeConnection();
                return;
            }

            // command "argumentOne" -> argumentOne
            Matcher argMatcher = Pattern.compile("(\\'|\\\").[^\"']*(\\'|\\\")").matcher(request);
            if (argMatcher.find()) {
                arg = argMatcher.group().subSequence(1, argMatcher.group().length() - 1).toString();
            }
            matchCommands(command, arg);
        }
    }

    /**
     * Closes the socket and bufferedWriter for a clean disconnection.
     */
    private void closeConnection() {

        try {
            FileShareMain.removeClient(socket.getInetAddress().getHostAddress());
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket!= null) socket.close(); // Also closes InputStream and OutputStream
        }
        catch (IOException ioException) {
            System.out.println("ConnectionHandler error when trying to close connection - " + ioException.getMessage());
        }
    }

    /**
     * Sends back a request verifying that the ConnectionHandler has been acknowledged by the client. Is ran when the
     * ConnectionHandler receives an acknowledge request.
     */
    private void acknowledge() {

        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        try {
            bufferedWriter.write("ACKNOWLEDGED\n");
            bufferedWriter.flush();
        }
        catch (IOException ioException) {
            System.out.println("ConnectionHandler error when responding to acknowledgement - " + ioException.getMessage());
        }
    }

    /**
     * Executes a specific method depending on the command provided and passes in any arguments if required.
     * @param command command sent by request
     * @param arg argument sent by request
     */
    private void matchCommands(String command, String arg) {

        if (command.equals("SEND")) {
            sendFile(arg);
        }

        if (command.equals("LIST")) {
            listFiles();
        }

        if (command.equals("ACKNOWLEDGEMENT_REQUEST")) {
            acknowledge();
        }

        if (command.equals("IS_HOSTED")) {
            isHostingFile(arg);
        }
    }

    /**
     * Sends the file size and bytes making up a file over the socket to the client using a BufferedWriter.
     * The file size is sent before the file bytes. This is so the client knows when how many bytes to read before
     * it should stop reading from the socket. If the file does not exist a file size of -1 is sent to tell the client
     * that the file does not exit.
     * @param fileName the name of the file present in the shared directory to be sent over the socket
     */
    private void sendFile(String fileName) {

        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        File file = new File(FileShareMain.getSharedDir() + "/" + fileName);
        long fileSize;
        if (file.exists()) {
            fileSize = file.length();
        } else {
            fileSize = -1; // A file size of -1 tells the client that the file does not exist
        }

        try {
            bufferedWriter.write(fileSize + "\n");
            bufferedWriter.flush();
        } catch (IOException ioException) {
            System.out.println("ConnectionHandler error when trying to write fileSize to socket - " + ioException.getMessage());
        }

        if (file.exists()) {
            byte[] bytes = new byte[65536];
            try {
                InputStream fileInputStream = new FileInputStream(file);

                int count;
                try {
                    while ((count = fileInputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, count);
                    }
                    outputStream.flush();
                } catch (IOException ioException) {
                    System.out.println("Server error when trying to write file to socket - " + ioException.getMessage());
                }
            }
            catch (FileNotFoundException fileNotFoundException) {
                System.out.println("Server error when trying to open file for sending - " + fileNotFoundException.getMessage());
            }
        }
    }

    /**
     * Sends a request to the client stating if a specified file is present in the shared directory of the server. If
     * true the string "HOSTED" is sent to the client and if not just an empty string is sent.
     * @param fileName
     */
    private void isHostingFile(String fileName) {

        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        try {
            File[] files = new File(FileShareMain.getSharedDir()).listFiles();
            ArrayList<String> fileNames = new ArrayList<String>();
            for (int i = 0; i < files.length; i++) {
                fileNames.add(files[i].getName());
            }

            if (fileNames.contains(fileName)) {
                bufferedWriter.write("HOSTED\n");
            }
            else {
                bufferedWriter.write("\n");
            }
            bufferedWriter.flush();
        }
        catch (IOException ioException) {
            System.out.println("Server error when trying to check if \'" + fileName + "\' is hosted on the server - " + ioException.getMessage());
        }
    }

    /**
     * Sends a list of the files present in the shared directory of the server to the client.
     */
    private void listFiles() {

        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        try {
            File[] files =  new File(FileShareMain.getSharedDir()).listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    bufferedWriter.write(files[i].getName() + " ");
                }
            }
            bufferedWriter.write("\n");
            bufferedWriter.flush();
        }
        catch (IOException ioException) {
            System.out.println("Server error when trying to list folders shared by server - " + ioException.getMessage());
        }
    }
}
