package com.mycompany.app;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/*
Wraps around connection to another client.
Uses a thread-per-connection model

Should be able to:
- Send messages to other connection
- Receive messages from other connection
- Maybe implement a keepAliveProtocol later on?
 */
public class Endpoint extends Thread{
    private final Socket socket;
    private final UUID uniqueID;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private BufferedReader commandLineReader;

    public Endpoint(Socket socket) {
        this.socket = socket;
        uniqueID = UUID.randomUUID();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error establishing new Endpoint's socket streams: " + e.getMessage());
        }
//        commandLineReader = new BufferedReader(new InputStreamReader(System.in));
        this.start();
    }

    @Override
    public void run() {

        while (!socket.isClosed()) {
            try {
                // TODO: Get this to work for reading text and sending it WITHOUT BLOCKING ON EITHER OPERATION


                String outgoingMessage = commandLineReader.readLine();
                if (!outgoingMessage.isEmpty()) {
                    bufferedWriter.write(outgoingMessage + "\n");
                    bufferedWriter.flush();
                }
                String incomingMessage = bufferedReader.readLine();
                System.out.println(incomingMessage);
            } catch (IOException e) {
                System.out.println("Error reading or writing to socket: " + e.getMessage());
            }
        }
        super.run();
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public BufferedReader getCommandLineReader() {
        return commandLineReader;
    }
}
