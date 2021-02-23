package com.mycompany.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
- Listens for connections from other clients
- When it gets a connection:
    wrap an endpoint around that connection
    pass that endpoint to ClientEndpointManager (each client should only have one)
 */
public class ClientServer extends Thread {
    private static ServerSocket serverSocket;
    private final ClientEndpointManager clientEndpointManager;

    public ClientServer(String hostName, int port, String localUsername, ClientEndpointManager clientEndpointManager) {
        this.clientEndpointManager = clientEndpointManager;
        startServerSocket(port);
        this.start();
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            acceptConnections();
        }
        super.run();
    }

    public void startServerSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error whilst starting client's ClientServer: " + e.getMessage());
        }
        System.out.println("Client's ClientServer listening for connections");
    }

    public void acceptConnections() {
        try {
            Socket socket = serverSocket.accept();
            clientEndpointManager.connectNewClientFromSocket(socket);
        } catch (IOException e) {
            System.out.println("Error whilst accepting connection from another client: " + e.getMessage());
        }
    }
}
