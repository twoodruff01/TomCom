package com.mycompany.app;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/*
java -cp target/TomCom-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.ClientMain -n ServySam
java -cp target/TomCom-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.ClientMain -lp 20001 -c -n Tom

Wraps around connection to another client, can:
- Send messages to other connection
- Receive messages from other connection
Each instance has two threads: one for reading console input, and one for getting messages from its socket
TODO: implement a keepAliveProtocol maybe?
 */
public class Endpoint extends Thread{
    private final Socket socket;
    private final String localUsername;
    private final ClientEndpointManager clientEndpointManager;
    private final UUID uniqueID;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String otherClientUsername = "Unknown User";
    private Thread consoleListenerThread;

    /*
    Message passing variable(s)
     */
    private static final String USERNAME_MESSAGE = "new_username";

    public Endpoint(Socket socket, String username, ClientEndpointManager clientEndpointManager) {
        this.socket = socket;
        localUsername = username;
        this.clientEndpointManager = clientEndpointManager;
        uniqueID = UUID.randomUUID();
        establishSocketReaders();
        this.start();
    }

    @Override
    public void run() {
        sendFromConsole();
        listenForMessages();
        consoleListenerThread.interrupt();
        super.run();
    }

    /*
    Read text input from console whilst socket open, and send over socket.
     */
    public void sendFromConsole() {
        consoleListenerThread = new Thread( () -> {
            BufferedReader commandLineReader = new BufferedReader(new InputStreamReader(System.in));
            sendUsername();  // Only need to do this once on start-up.
            while (!interrupted()) {
                String outgoingMessage = null;
                try {
                    outgoingMessage = commandLineReader.readLine();
                } catch (IOException e) {
                    System.out.println("Error reading input from your console: " + e.getMessage());
                }
                if (outgoingMessage != null) {
                    sendString(outgoingMessage);
                    /*
                    Maybe should just broadcast by default so can send to all endpoints that have connected?
                    Like this:
                        clientEndpointManager.broadcast(outgoingMessage);
                     */
                }
            }
        });
        consoleListenerThread.start();
    }

    /*
    Read messages from open socket.
     */
    public void listenForMessages() {
        while (!socket.isClosed()) {
            String incomingMessage = null;
            try {
                incomingMessage = bufferedReader.readLine();
            } catch (IOException e) {
                System.out.println("Error reading from socket: " + e.getMessage());
            }
            if (incomingMessage != null && isUsername(incomingMessage)) {
                receiveUsername(incomingMessage);
            } else if (incomingMessage != null) {
                System.out.println("User" + " " + otherClientUsername + ": " + incomingMessage);
            }
        }
    }

    private void establishSocketReaders() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error establishing new Endpoint's socket streams: " + e.getMessage());
        }
    }

    private void sendString(String message) {
        try {
            bufferedWriter.write(message + "\n");  // Shared object between threads
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Error writing to socket: " + e.getMessage() + ": Closing connection");
            closeSocket();
        }
    }

    /*
    Could put all this messaging stuff in its own class later maybe?
     */
    private boolean isUsername(String message) {
        return message.split(",")[0].equals(USERNAME_MESSAGE);
    }

    /*
    Sends this client's username to the other connection, so that connection knows who it's connected to
    Message should be in format: "USERNAME_MESSAGE,greg"
     */
    private void sendUsername() {
        String usernameMessage = USERNAME_MESSAGE + "," + localUsername;
        sendString(usernameMessage);
    }

    private void receiveUsername(String message) {
        otherClientUsername = message.split(",")[1]; // This could give arrayIndexOutOfBounds if -n flag is given no argument?
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Failed to close socket: " + e.getMessage());
        }
    }

    public UUID getUniqueID() {
        return uniqueID;
    }
}
