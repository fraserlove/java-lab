import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A client class that creates a connection through a socket with a server to send and receive requests. The class can
 * also disconnect from a socket manually and automatically when it detects that the server has closed. The client
 * will also carry out different operations such as receiving a file sent over as bytes from the server and receiving
 * the list of files available on the server.
 */
public class Client {

    private Socket socket; // sends and receives data to and from the server
    private String serverAddress;
    private int port;

    private InputStream inputStream; // receives data from server through socket
    private OutputStream outputStream; // sends data to server through socket
    private BufferedWriter bufferedWriter; // allows data to be written to the output stream

    public Client(int port) { this.port = port; }

    public boolean isConnected() { return socket != null; }

    /**
     * Connects to the a server at the specified address on a predefined port through a socket. A timeout of 2s is given
     * before a timeout error occurs and the client stops trying to connect.
     * @param serverAddress the address of the peer to connect to
     * @param silent specifies if a message should be displayed to screen if the connection was successful, used when
     *               wanting to silently test a connection to a server when scanning the local network for peers that
     *               have a running server and can send and receive files
     */
    public void connect(String serverAddress, boolean silent) {

        this.serverAddress = serverAddress;

        try {
            if (InetAddress.getByName(serverAddress).isReachable(2000)) {
                socket = new Socket(serverAddress, port);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                if (!silent) {
                    System.out.println("Successfully connected to " + serverAddress);
                }
            }
            else {
                System.out.println("Unable to connect to " + serverAddress + " - timed out");
            }
        }
        catch (IOException ioException) {
            System.out.println("Unable to connect to " + serverAddress + " - " + ioException.getMessage());
        }
    }

    /**
     * Cleanly disconnects from the server by closing the socket and BufferedWriter.
     * @param silent specifies if a message should be displayed to screen if the client disconnect from the server,
     *               used when wanting to silently test a connection to a server when scanning the local network for
     *               peers that have a running server and can send and receive files
     */
    public void disconnect(boolean silent) {

        try {
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close(); // Also closes InputStream and OutputStream
            socket = null;
            if (!silent) {
                System.out.println("Successfully disconnected from " + serverAddress);
            }
            FileShareMain.removeClient(serverAddress);
        }
        catch (IOException ioException) {
            System.out.println("Client error when trying to close socket - " + ioException.getMessage());
        }
    }

    /**
     * Checks if a connection can be made to a specified peer. This peer can either be at a specified address or can
     * be the server address that the client is connected to currently. This method has two uses. Firstly it checks to
     * see if a connection can still be made to the peers server, thereby checking if the server is still running,
     * allowing the client to disconnect from the server if the server has been closed. Secondly, it can check if a
     * server is running at a specified peer address. This is used when scanning all of the computers on the local
     * network to check for running servers that can be connected to.
     * @param address the address of the peer being checked, is either an empty string (stating to just check the peer)
     *                that the client is already connected to, or a new address of another peer
     * @return a boolean stating if a connection can be made to the specified peer
     */
    public boolean connectionOpen(String address) {

        try {
            Socket tempSocket;
            if (address.equals("")) {
                tempSocket = new Socket(serverAddress, port);
            } else {
                tempSocket = new Socket(address, port);
            }
            OutputStream tempOutputStream = tempSocket.getOutputStream();
            InputStream tempInputStream = tempSocket.getInputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(tempOutputStream));
            bufferedWriter.write("ACKNOWLEDGEMENT_REQUEST\n");
            bufferedWriter.flush();

            String response = new BufferedReader(new InputStreamReader(tempInputStream)).readLine();
            bufferedWriter.close();
            tempSocket.close(); // closes output and input streams as well

            if (response.equals("ACKNOWLEDGED")) {
                return true;
            }
            else {
                System.out.println("An invalid application at " + serverAddress + " is running.");
                return false;
            }
        }
        catch (IOException ioException) {
            if (socket != null && address.equals("")) {
                System.out.println("Server " + serverAddress + " has closed.");
                disconnect(false);
            }
        }
        return false;
    }

    /**
     * Checks if a specific file is being shared by a peer or not.
     * @param fileName the file name of the file that's presence on the server is being queried
     * @return a boolean stating if the peer is sharing the specified file
     */
    public boolean isPeerSharingFile(String fileName) {

        makeRequest("IS_HOSTED", fileName);
        try {
            String response = new BufferedReader(new InputStreamReader(inputStream)).readLine();
            if (response.equals("HOSTED")) {
                return true;
            }
        }
        catch (IOException ioException) {
            System.out.println("Client error when trying to read list of files available on server - " + ioException.getMessage());
        }
        return false;
    }

    /**
     * Gets a list of all of the files shared by the peer by reading all of the files available from a response sent by
     * the server.
     * @return a list of the files that are shared by the peer
     */
    public ArrayList<String> getSharedFiles() {

        makeRequest("LIST", "");
        try {
            String response = new BufferedReader(new InputStreamReader(inputStream)).readLine();
            ArrayList<String> files = new ArrayList<>(Arrays.asList(response.split(" ")));
            return files;
        }
        catch (IOException ioException) {
            System.out.println("Client error when trying to read list of files available on server - " + ioException.getMessage());
            return null;
        }
    }

    /**
     * Downloads a specified file from the server. A request is first sent to the server, to send the file that matches
     * the file name given as an argument. The method then reads in a file size sent in from the server. If the file
     * size is -1 then the file is not present on the server. Otherwise the file then reads in the bytes representing
     * the file and whilst also displaying a dynamic progress bar to track the download in the command line. More info
     * such as the download speed in MB/s, total downloaded in MB, percentage downloaded and seconds remaining until
     * the download is estimated to finish are shown.
     * @param fileName the name of the file being downloaded
     */
    public void downloadFile(String fileName) {

        makeRequest("SEND ", fileName);

        int chunk;
        int byteCount = 0;
        int latestByteCount = 0;
        int numberOfBars;
        int totalBars = 35;

        int MBPerSecond = 0;
        int MBDownloaded = 0;
        int percentageDownloaded = 0;
        int secondsRemaining = 0;

        String percentageBar = "";

        try {
            long fileSize = Integer.parseInt(new BufferedReader(new InputStreamReader(inputStream)).readLine());
            if (fileSize == -1) { // If the fileSize sent from the server is -1 then the file does not exist on the server
                System.out.println("File does not exist, list all files available from peer with \'available\'");
                return;
            }

            long longUpdateTime = (long) (System.nanoTime() - 2.5e8); // Taking 0.25s off so that updated values are shown immediately
            long shortUpdateTime = (long) (System.nanoTime() - 1e8); // Taking 0.01s off so that updated values are shown immediately

            FileOutputStream fileOutputStream = new FileOutputStream(FileShareMain.getSavedDir() + "/" + fileName);
            byte[] bytes = new byte[65536];

            System.out.println("Downloading " + fileName +  " [" + fileSize / 1000000 + "MB]");
            while ((chunk = inputStream.read(bytes)) != -1) {

                fileOutputStream.write(bytes, 0, chunk);
                byteCount += chunk;

                MBDownloaded = byteCount / 1000000;
                percentageDownloaded = (int) Math.round((double) byteCount / (double) fileSize * 100);
                numberOfBars = Math.round(percentageDownloaded * totalBars / 100);
                percentageBar = " |" + new String(new char[numberOfBars]).replace("\0", "█") + new String(new char[totalBars - numberOfBars]).replace("\0", " ") + "| ";

                if ((System.nanoTime() - longUpdateTime) / 250000000 > 1) { // Updating every 0.25s to give time to read their values before they update
                    MBPerSecond = Math.round((byteCount - latestByteCount) * 4 / 1000000);
                    latestByteCount = byteCount;
                    if (MBPerSecond != 0) {
                        secondsRemaining = (int) ((fileSize / 1000000) - MBDownloaded) / MBPerSecond;
                    } else {
                        secondsRemaining = -1; // If MBPS is equal to zero just set the time remaining to be -1 to show error with transmission
                    }
                    longUpdateTime = System.nanoTime();
                }

                if ((System.nanoTime() - shortUpdateTime) / 10000000 > 1) { // Updating every 0.01s for fluid updates that are performant and do not lag the console
                    System.out.print("    " + percentageDownloaded + "%" + percentageBar + MBDownloaded + "MB " + MBPerSecond + "MB/s eta " + secondsRemaining + "s      \r"); // Extra spaces remove any past characters that have not been written over
                    shortUpdateTime = System.nanoTime();
                }

                if (byteCount == fileSize) {
                    break;
                }
            }
            fileOutputStream.flush();
            System.out.print("    " + percentageDownloaded + "%" + percentageBar + MBDownloaded + "MB " + MBPerSecond + "MB/s Done.     \n");

        }
        catch (IOException ioException) {
            System.out.println("Client error when trying to read file from server - " + ioException.getMessage());
        }
    }

    /**
     * Sends a request to the server
     * @param request a string that is the request to be sent
     * @param arg an argument that is to be sent with the request
     */
    private void makeRequest(String request, String arg) {

        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(request + " \"" + arg + "\"\n");
            bufferedWriter.flush();
        }
        catch (IOException ioException) {
            System.out.println("Client error when trying to send command to server - " + ioException.getMessage());
        }
    }
}
