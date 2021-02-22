package com.mycompany.app;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.Socket;

/*
- Parses CLI input
- Starts ClientServer
- Starts ClientEndpointManager
- TODO: implement a centralised server, and connect to all registered clients on start-up
 */
public class ClientMain {
    private static final String DEFAULT_HOST_NAME = "localhost";
    private static final int DEFAULT_PORT = 30000;

    public static void main( String[] args ) throws ParseException {
        String listenHost = DEFAULT_HOST_NAME;
        String connectionHost = DEFAULT_HOST_NAME;
        int listenPort = DEFAULT_PORT;
        int connectionPort = DEFAULT_PORT;

        Options options = new Options();
        options.addOption("lh", "listen-host", true, "Host-name for this client to use");
        options.addOption("lp", "listen-port", true, "Port for client to listen on");
        options.addOption("c", "connect", false, "Start-up and connect to another client");
        options.addOption("ch", "connection-host", true, "Remote host-name to connect to");
        options.addOption("cp", "connection-port", true, "Remote port to connect to");

        // TODO: use this to name sessions later maybe?
        options.addOption("n", "name", true, "Enter your client's name");

        // TODO: requires a centralised server to be implemented
        options.addOption("ca", "connect-all", false, "Connect to all existing clients");

        // Sanity check whilst developing
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("TomCom", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);

        if (cmd.hasOption("lh")) {
            listenHost = cmd.getOptionValue("lh");
        }
        if (cmd.hasOption("lp")) {
            listenPort = Integer.parseInt(cmd.getOptionValue("lp"));
        }

        // At this point we can start listening for connections regardless of whether we're connecting to anyone
        ClientEndpointManager clientEndpointManager = new ClientEndpointManager();
        ClientServer clientServer = new ClientServer(listenHost, listenPort, clientEndpointManager);

        if (cmd.hasOption("c")) {
            if (cmd.hasOption("ch")) {
                connectionHost = cmd.getOptionValue("ch");
            }
            if (cmd.hasOption("cp")) {
                connectionPort = Integer.parseInt(cmd.getOptionValue("cp"));
            }

            try {
                Socket newConnection = new Socket(connectionHost, connectionPort);
                Endpoint endpoint = new Endpoint(newConnection);
                clientEndpointManager.addEndpoint(endpoint);
            } catch (IOException e) {
                System.out.println("Error whilst trying to connect to other client " + e.getMessage());
            }
        }
    }
}
