package com.mycompany.app;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/*
Wraps around both ends of a connection to another client
- mainly listens for messages from other end of the endpoint
- can also send messages
Calling sendString() in this class only communicates with the other end of the socket (one-to-one communication)
TODO: implement a keepAliveProtocol maybe?
 */
public class Endpoint extends Thread{
    private final Socket socket;
    private final String localUsername;
    private final UUID uniqueID;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String otherClientUsername = "Unknown User";

    /*
    Message passing variable(s)
     */
    private static final String USERNAME_MESSAGE = "new_username";

    public Endpoint(Socket socket, String username) {
        this.socket = socket;
        localUsername = username;
        uniqueID = UUID.randomUUID();
        establishSocketReaders();
        this.start();
    }

    @Override
    public void run() {
        sendUsername();
        listenForMessages();
        super.run();
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

    public void sendString(String message) {
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
    Only need to do this once on start-up.
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
