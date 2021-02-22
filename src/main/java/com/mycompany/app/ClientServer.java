package com.mycompany.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
- Listens for connections from other clients
- When it gets a connection:
    wrap an endpoint around that connection
    pass that endpoint to EndpointManager (each client should only have one EndpointManager)
- Repeat above indefinitely...
 */
public class ClientServer extends Thread {
    private static ServerSocket serverSocket;
    private final ClientEndpointManager clientEndpointManager;


    public ClientServer(String hostName, int port, ClientEndpointManager clientEndpointManager) {
        this.clientEndpointManager = clientEndpointManager;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error whilst starting client's ClientServer: " + e.getMessage());
        }

        System.out.println("Client's ClientServer listening for connections");
        this.start();
    }


    @Override
    public void run() {

        while (!serverSocket.isClosed()) {

            try {
                Socket newConnectionSocket = serverSocket.accept();
                Endpoint newEndpoint = new Endpoint(newConnectionSocket);
                clientEndpointManager.addEndpoint(newEndpoint);
            } catch (IOException e) {
                System.out.println("Error whilst accepting connection from another client: " + e.getMessage());
            }

        }
        super.run();
    }
}
