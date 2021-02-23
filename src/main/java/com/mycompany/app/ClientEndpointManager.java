package com.mycompany.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
Keeps track of every Endpoint.
Also starts a consoleListenerThread that reads text from CLI and sends it to every connected endpoint
 */
public class ClientEndpointManager extends Thread{
    private final ConcurrentHashMap<UUID, Endpoint> concurrentHashMap;
    private final String localUsername;

    public ClientEndpointManager(String localUsername) {
        concurrentHashMap = new ConcurrentHashMap<>();
        this.localUsername = localUsername;
        this.start();
    }

    @Override
    public void run() {
        handleConsoleInput();
        super.run();
    }

    /*
    Continuously read text input from console and send it to all registered Endpoints
    This needs its own thread because it blocks on the readLine() call, and I need ClientEndpointManager to be able
    to accept new Endpoints in the ClientServer code.
    TODO: maybe make this its own class?
     */
    public void handleConsoleInput() {
        Thread consoleListenerThread = new Thread( () -> {
            BufferedReader commandLineReader = new BufferedReader(new InputStreamReader(System.in));
            while (!interrupted()) {
                String outgoingMessage = null;
                try {
                    outgoingMessage = commandLineReader.readLine();
                } catch (IOException e) {
                    System.out.println("Error reading input from your console: " + e.getMessage());
                }
                if (outgoingMessage != null) {
                    tellEveryone(outgoingMessage);
                }
            }
        });
        consoleListenerThread.start();
    }

    /*
    Send this message to every Endpoint the manager knows about
     */
    public void tellEveryone(String message) {
        for (Endpoint endpoint : concurrentHashMap.values()) {
            endpoint.sendString(message);
        }
    }

    private void addEndpoint(Endpoint endpoint) {
        System.out.println("Adding endpoint to ClientEndpointManager");
        concurrentHashMap.put(endpoint.getUniqueID(), endpoint);
    }

    public void connectNewClientFromSocket(Socket socket) {
        Endpoint newEndpoint = new Endpoint(socket, localUsername);
        addEndpoint(newEndpoint);
    }

    public void connectNewClientDirectly(String connectionHost, int connectionPort) {
        try {
            Socket newConnection = new Socket(connectionHost, connectionPort);
            Endpoint endpoint = new Endpoint(newConnection, localUsername);
            addEndpoint(endpoint);
        } catch (IOException e) {
            System.out.println("Failed connect to other client " + e.getMessage());
        }
    }
}
