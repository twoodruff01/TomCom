package com.mycompany.app;
import org.apache.commons.cli.*;

/*
- Parses CLI input
- Starts ClientServer
- Starts ClientEndpointManager
 */
public class ClientMain {
    private static final String DEFAULT_HOST_NAME = "localhost";
    private static final int DEFAULT_PORT = 30000;

    public static void main( String[] args ) {
        String listenHost = DEFAULT_HOST_NAME;
        String connectionHost = DEFAULT_HOST_NAME;
        int listenPort = DEFAULT_PORT;
        int connectionPort = DEFAULT_PORT;

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("l", "listen", false, "Start-up and only listen for connections");
        options.addOption("lh", "listen-host", true, "Host-name for this client to use");
        options.addOption("lp", "listen-port", true, "Port for client to listen on");

        options.addOption("c", "connect", false, "Start-up and connect to another client");
        options.addOption("ch", "connection-host", true, "Remote host-name to connect to");
        options.addOption("cp", "connection-port", true, "Remote port to connect to");

        options.addOption("ca", "connect-all", false, "Connect to all existing clients");  // Requires a centralised server to be implemented

        try {
            CommandLine cmd = parser.parse( options, args);
        } catch (ParseException e) {
            System.out.println("Error instantiating CommandLine: " + e.getMessage());
        }

        if (options.hasOption("lh")) {
            listenHost = options.getOption("lh").getValue();
        }
        if (options.hasOption("lp")) {
            listenPort = Integer.parseInt(options.getOption("lp").getValue());
        }

        ClientServer clientServer = new ClientServer(listenHost, listenPort);

        if (options.hasOption("l")) {
            // startup and just listen for other connections

        } else if (options.hasOption("c")) {
            if (options.hasOption("ch")) {
                connectionHost = options.getOption("ch").getValue();
            }
            if (options.hasOption("cp")) {
                connectionPort = Integer.parseInt(options.getOption("cp").getValue());
            }

            // startup, listen for other connections, and connect to specified client

//             ClientEndpointManager clientEndpointManager = new ClientEndpointManager();
//            Endpoint endpoint = new Endpoint();
//            clientEndpointManager.passEndpoint(endpoint);
        }
    }
}
