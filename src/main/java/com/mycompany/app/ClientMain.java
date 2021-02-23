package com.mycompany.app;
import org.apache.commons.cli.*;

import java.util.concurrent.ThreadLocalRandom;

//java -cp target/TomCom-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.ClientMain -n ServySam
//java -cp target/TomCom-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.ClientMain -lp 20001 -c -n Tom

/*
- Parses CLI input
- Starts ClientServer
- Starts ClientEndpointManager
- Connects to just one specified client, if directed to
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
        String userName = Integer.toString(ThreadLocalRandom.current().nextInt(1000, 9999));

        Options options = new Options();
        options.addOption("h", "help", false, "Show Info");
        options.addOption("lh", "listen-host", true, "Host-name for this client to use");
        options.addOption("lp", "listen-port", true, "Port for client to listen on");
        options.addOption("c", "connect", false, "Start-up and connect to another client");
        options.addOption("ch", "connection-host", true, "Remote host-name to connect to");
        options.addOption("cp", "connection-port", true, "Remote port to connect to");
        options.addOption("n", "name", true, "Enter your client's name");
        // TODO: requires a centralised server to be implemented
        options.addOption("ca", "connect-all", false, "Connect to all existing clients");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);
        HelpFormatter formatter = new HelpFormatter();
        if (cmd.hasOption("h")) {
            formatter.printHelp("TomCom", options);
            // Could also print out hostname here to help with networking stuff
            System.exit(0);
        }
        if (cmd.hasOption("lh")) {
            listenHost = cmd.getOptionValue("lh");
        }
        if (cmd.hasOption("lp")) {
            listenPort = Integer.parseInt(cmd.getOptionValue("lp"));
        }
        if (cmd.hasOption("n")) {
            userName = cmd.getOptionValue("n");
        }

        // At this point we can start listening for connections regardless of whether we're connecting to anyone
        ClientEndpointManager clientEndpointManager = new ClientEndpointManager(userName);
        ClientServer clientServer = new ClientServer(listenHost, listenPort, userName, clientEndpointManager);

        if (cmd.hasOption("c")) {
            if (cmd.hasOption("ch")) {
                connectionHost = cmd.getOptionValue("ch");
            }
            if (cmd.hasOption("cp")) {
                connectionPort = Integer.parseInt(cmd.getOptionValue("cp"));
            }
            clientEndpointManager.connectNewClientDirectly(connectionHost, connectionPort);
        }
    }
}
