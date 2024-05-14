import woodland.server.GameServer;

/**
 * The entry point to run a woodland diplomacy server.
 */
public class GameServerMain {

    private static final int MAX_PORT = 65535;

    /**
     * Starts a woodland diplomacy server. Expects two command line arguments, the
     * port number for the server to listen on and a seed number for game randomness.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java GameServerMain <port> <seed>");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
            if (port <= 0 || port > MAX_PORT) {
                System.out.println("Invalid port number. Port should be between 1 and 65535.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Port should be an integer.");
            return;
        }

        long seed;
        try {
            seed = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Seed should be a long.");
            return;
        }

        new GameServer(port, seed);
    }
}
