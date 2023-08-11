import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The file sharing application that runs the client and the server provides a textual interface with commands that
 * allow the user to control the application. The application first creates and starts a server and creates a client.
 * Then the application also loads a config file detailing which port the application is running on and the path to
 * the saved and shared directories. Finally the textual interface is ran which allows the user to enter commands and
 * displays relevant output.
 *
 * @author 200002548
 *
 */
public class FileShareMain {

    private static Set<String> baseCommands = Set.of("exit", "downloads", "dirs", "change-dirs", "serving", "peers", "search", "list");
    private static Set<String> disconnectedCommands = Stream.concat(Set.of("connect").stream(), baseCommands.stream()).collect(Collectors.toSet());
    private static Set<String> connectedCommands = Stream.concat(Set.of("disconnect", "get", "available").stream(), baseCommands.stream()).collect(Collectors.toSet());

    private static Client client;
    private static Server server;
    private static int port = 9999;

    private static Properties config;

    private static String savedDir;
    private static String sharedDir;

    private static String output;

    public static String getSavedDir() { return savedDir; }

    public static String getSharedDir() { return sharedDir; }

    public static void removeClient(String client) { server.removeClient(client); }

    /**
     * Loads the config file, extracting the saved and shared directory paths, as well as the port used by the
     * application used to connect to peers.
     */
    private static void loadConfig() {

        try {
            config = new Properties();
            String propertiesFileName = "p2p.config";
            InputStream IS;
            IS = new FileInputStream(propertiesFileName);
            config.load(IS);

            savedDir = config.getProperty("savedDir");
            sharedDir = config.getProperty("sharedDir");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * The main function from which the application is launched. It creates a server and a client class and starts the
     * server, then makes the saved and shared directories if they are not already present in the file system. Finally
     * it displays a welcome screen and runs the textual interface.
     * @param args
     */
    public static void main(String[] args) {

        loadConfig();
        server = new Server(port);
        server.start();
        client = new Client(port);

        new File(config.getProperty("savedDir")).mkdirs();
        new File(config.getProperty("sharedDir")).mkdirs();

        System.out.print("\n" +
                "  ██████╗ ██████╗ ██████╗     ███████╗██╗██╗     ███████╗\n" +
                "  ██╔══██╗╚════██╗██╔══██╗    ██╔════╝██║██║     ██╔════╝\n" +
                "  ██████╔╝ █████╔╝██████╔╝    █████╗  ██║██║     █████╗  \n" +
                "  ██╔═══╝ ██╔═══╝ ██╔═══╝     ██╔══╝  ██║██║     ██╔══╝  \n" +
                "  ██║     ███████╗██║         ██║     ██║███████╗███████╗\n" +
                "  ╚═╝     ╚══════╝╚═╝         ╚═╝     ╚═╝╚══════╝╚══════╝\n" +
                "                                                         \n" +
                "  ███████╗██╗  ██╗ █████╗ ██████╗ ██╗███╗   ██╗ ██████╗  \n" +
                "  ██╔════╝██║  ██║██╔══██╗██╔══██╗██║████╗  ██║██╔════╝  \n" +
                "  ███████╗███████║███████║██████╔╝██║██╔██╗ ██║██║  ███╗ \n" +
                "  ╚════██║██╔══██║██╔══██║██╔══██╗██║██║╚██╗██║██║   ██║ \n" +
                "  ███████║██║  ██║██║  ██║██║  ██║██║██║ ╚████║╚██████╔╝ \n" +
                "  ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝  \n" +
                "                                                         \n");
        System.out.println("  Connect to a peer using 'connect' followed by the peers address.");
        System.out.println("  List all available commands with 'list'.\n");

        runInterface();
    }

    /**
     * A method outlining the textual interface itself. It receives user input from standard input and parses it to
     * extract a command and arguments. It checks to see if what was entered featured a valid command or if the command
     * can be used currently. These commands and arguments are then sent to the match command method that runs the
     * command.
     */
    private static void runInterface() {

        String command = "";
        ArrayList<String> args;
        String input = "";

        while (true) {
            output = "";
            args = new ArrayList<>();

            System.out.print(">> ");
            try {
                input = new BufferedReader(new InputStreamReader(System.in)).readLine();
                // Check if client is connected to a peer, since different commands are available if the client is connected to a peer or not.
                if (client.isConnected()) {
                    while (!connectedCommands.contains(command = input.replaceAll(" .*", ""))) {
                        // If the command entered is unavailable while connected to a peer, display that this command cannot be ran.
                        if (disconnectedCommands.contains(command)) {
                            System.out.print("Invalid command, this command can only be ran when disconnected from a peer.\n>> ");
                        }
                        else {
                            System.out.print("Invalid command, type 'list' to view all commands.\n>> ");
                        }
                        input = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    }
                }
                else {
                    while (!disconnectedCommands.contains(command = input.replaceAll(" .*", ""))) {
                        // If the command entered is unavailable until connected to a peer, display that a peer must first be connected to.
                        if (connectedCommands.contains(command)) {
                            System.out.print("Invalid command, connect to a peer first before issuing this command.\n>> ");
                        }
                        else {
                            System.out.print("Invalid command, type 'list' to view all commands.\n>> ");
                        }
                        input = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    }
                }
            }
            catch (IOException ioException) {
                System.out.println("Backend error when trying to validate entered command - " + ioException.getMessage());
            }

            // command "argumentOne" "argumentTwo" -> {argumentOne, argumentTwo}
            Matcher argMatcher = Pattern.compile("(\\'|\\\").[^\"']*(\\'|\\\")").matcher(input);
            while (argMatcher.find()) {
                args.add(argMatcher.group().subSequence(1, argMatcher.group().length() - 1).toString());
            }

            matchCommands(command, args);
            // Any output must then be displayed to the command line. This output is collected and displayed this way
            // as it makes the code cleaner and allows for better manipulation of the output before it is sent to the
            // command line.
            if (output != "") {
                System.out.println(output);
            }
        }
    }

    /**
     * Matches commands to methods, as well as passing in any arguments provided.
     * @param command command entered, corresponding to method that must be ran
     * @param args any arguments tha must be passed into the corresponding method
     */
    private static void matchCommands(String command, ArrayList<String> args) {

        if (command.equals("connect")) {
            if (args.size() > 0) {
                client.connect(args.get(0), false);
            } else {
                System.out.println("Must provide a peer address to connect to.");
            }
        }

        else if (command.equals("list")) {
            listCommands();
        }

        else if (command.equals("exit")) {
            exit();
        }

        else if (command.equals("serving")) {
            listConnectedPeers();
        }

        else if (command.equals("peers")) {
            listAvailablePeers();
        }

        else if (command.equals("search")) {
            if (args.size() > 0) {
                searchForFile(args.get(0));
            } else {
                System.out.println("Must provide a file to search for.");
            }
        }

        else if (command.equals("downloads")) {
            listDownloads();
        }

        else if (command.equals("dirs")) {
            output = "   Saved: " + getSavedDir() + "\tShared: " + getSharedDir();
        }

        else if (command.equals("change-dirs")) {
            if (args.size() > 1) {
                changeDirectories(args.get(0), args.get(1));
            } else {
                output = "Must provide a path to a shared and saved directory";
            }
        }

        // Check if peer is still connected before carrying out these specific commands.
        if (client.connectionOpen("")) {

            if (command.equals("disconnect")) {
                client.disconnect(false);
            }

            else if (command.equals("get")) {
                if (args.size() > 0) {
                    client.downloadFile(args.get(0));
                } else {
                    System.out.println("Must provided a file name to download from peer.");
                }
            }

            else if (command.equals("available")) {
                listPeersFiles();
            }
        }
    }

    /**
     * Exits the program cleanly by disconnecting the client from the peer (if the client is connected to a peer) and
     * closing down the server.
     */
    private static void exit() {

        if (client != null) {
            if (client.isConnected()) {
                client.disconnect(false);
            }
        }
        server.close();
        System.exit(0);
    }

    /**
     * Changes the directories where files are saved to and files are shared from. This change is applied to the
     *
     * @param savedPath a relative path from the folder holding the class files to the saved directory
     * @param sharedPath a relative path from the folder holding the class files to the shared directory
     */
    private static void changeDirectories(String savedPath, String sharedPath) {

        savedDir = savedPath;
        sharedDir = sharedPath;
        config.setProperty("savedDir", savedDir);
        config.setProperty("sharedDir", sharedDir);

        try {
            config.store(new FileOutputStream("p2p.config"), null);
            new File(config.getProperty("savedDir")).mkdirs();
            new File(config.getProperty("sharedDir")).mkdirs();
            output = "Successfully changed shared and saved directories";
        }
        catch (IOException ioException) {
            System.out.println("Backend error when changing directories - " + ioException.getMessage());
        }
    }

    /**
     * Lists all of the commands available to the user. These can vary depending on if the client is connected to a
     * peer.
     */
    private static void listCommands() {
        output = "   exit                                        quit the application.\n" +
                 "   downloads                                   list all files downloaded that are present in the saved directory.\n" +
                 "   dirs                                        show the location of the shared and saved directories.\n" +
                 "   change-dirs \"<saved-dir>\" \"<shared-dir>\"    changes the location where files are saved and shared.\n" +
                 "   serving                                     list peers that are currently connected to this client.\n" +
                 "   peers                                       list all peers on the local network.\n" +
                 "   search \"<file-name>\"                        list all peers on the network hosting a specific file.\n" +
                 "   list                                        list all commands available to the user.\n";
        if (client.isConnected()) {
            output = output.concat("   disconnect                                  disconnect the peer currently connected to.\n" +
                                   "   get \"<file-name>\"                           download the specified file from the peer currently connected to. \n" +
                                   "   available                                   list files shared by the peer and are available to download.");
        }
        else {
            output = output.concat("   connect \"<peer-address>\"                    connect to a peer with the specified address.");
        }
    }

    /**
     * Lists all files that are being shared by the peer. These files are what can be downloaded by the client.
     */
    private static void listPeersFiles() {
        ArrayList<String> files = client.getSharedFiles();
        output = "   (" + files.size() + ") ";
        for (String file : files) {
            output = output.concat(file + " ");
        }
        if (files.size() == 0) {
            output = "   No files found.";
        }
    }

    /**
     * Lists all files present in the saved directory, these are the files that have been downloaded from peers.
     */
    private static void listDownloads() {

        File[] files = new File(getSavedDir()).listFiles();
        output = "   (" + files.length + ") ";
        int fileCount = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                output = output.concat(files[i].getName() + " ");
                fileCount++;
            }
        }
        if (fileCount == 0) {
            output = "   No files found.";
        }
    }

    /**
     * Lists all peers that are currently connected to the server.
     */
    private static void listConnectedPeers() {

        ArrayList<String> connectedPeers = server.getClients();
        output = "   (" + connectedPeers.size() + ") ";
        if (connectedPeers.size() > 0) {
            for (String peer : connectedPeers) {
                output = output.concat(peer + " ");
            }
        }
        else {
            output = "   No peers connected.";
        }
    }

    /**
     * Lists all peers that are on the local network that have instances of this application running. This is done by
     * port scanning all of the machines on the local network and checking if the client can make a connection to a
     * reachable computer. The client sends an acknowledgement request to the computer in question and if it receives a
     * valid reply, it is running an instance of this program and is a peer that can be connected to. It also returns
     * this list so that it can be used in the searchForFile method.
     * @return a set of the addresses of computers in the local network that are running the application and have a
     * server open that can be connected to
     */
    private static Set<String> listAvailablePeers() {

        Set<String> availablePeers = new HashSet<String>();
        try {
            String hostIP = InetAddress.getLocalHost().getHostAddress();
            String subnet = hostIP.substring(0, hostIP.lastIndexOf("."));

            for (int i = 0; i < 255; i++) {
                String ipAddress = subnet + "." + i;
                if (InetAddress.getByName(ipAddress).isReachable(1)) {
                    if (!ipAddress.equals(hostIP) && client.connectionOpen(ipAddress)) {
                        availablePeers.add(ipAddress);
                    }
                }
            }
        }
        catch (IOException ioException) {
            output = "Backend error listing when peers available on the network - " + ioException.getMessage();
        }

        output = "   (" + availablePeers.size() + ") ";
        if (availablePeers.size() > 0) {
            for (String peer : availablePeers) {
                output = output.concat(peer + " ");
            }
        }
        else {
            output = "   No peers available.";
        }
        return availablePeers;
    }

    /**
     * Searches over all of the peers in the local network for a specific file. The peers that are hosting this file
     * (identified by its file name) are then displayed to the command line. This is done by receiving the set of peers
     * from the listAvailablePeers method and using a temporary client to send a request to each peers server to check
     * if the peer is sharing this specific file.
     * @param fileName the file name to be searching for in peers on the local network
     */
    private static void searchForFile(String fileName) {

        Set<String> availablePeers = listAvailablePeers();
        Set<String> peersWithFile = new HashSet<>();
        Client tempClient = new Client(port);

        for (String peerIP : availablePeers) {
            tempClient.connect(peerIP, true);
            if (tempClient.isPeerSharingFile(fileName)) {
                peersWithFile.add(peerIP);
            }
            tempClient.disconnect(true);
        }

        output = "   (" + peersWithFile.size() + ") ";
        if (peersWithFile.size() > 0) {
            for (String peer : peersWithFile) {
                output = output.concat(peer + " ");
            }
        }
        else {
            output = "   No peers with '" + fileName + "' file found";
        }
    }
}