package com.mycompany.app;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
Keeps track of every Endpoint / connection that this client has.
 */
public class ClientEndpointManager extends Thread{
    private final ConcurrentHashMap<UUID, Endpoint> concurrentHashMap;
    private final String localUsername;
    private boolean isBroadcasting;

    public ClientEndpointManager(String localUsername) {
        concurrentHashMap = new ConcurrentHashMap<>();
        this.localUsername = localUsername;
        isBroadcasting = false;
        this.start();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (isBroadcasting) {
                broadcast("nothing");
                isBroadcasting = false;
            }
        }
        super.run();
    }

    private void addEndpoint(Endpoint endpoint) {
        System.out.println("Adding endpoint to ClientEndpointManager");
        concurrentHashMap.put(endpoint.getUniqueID(), endpoint);
    }

    public void connectNewClientFromSocket(Socket socket) {
        Endpoint newEndpoint = new Endpoint(socket, localUsername, this);
        addEndpoint(newEndpoint);
    }

    public void connectNewClientDirectly(String connectionHost, int connectionPort) {
        try {
            Socket newConnection = new Socket(connectionHost, connectionPort);
            Endpoint endpoint = new Endpoint(newConnection, localUsername, this);
            addEndpoint(endpoint);
        } catch (IOException e) {
            System.out.println("Failed connect to other client " + e.getMessage());
        }
    }

    private void broadcast(String message) {
        for (Endpoint endpoint : concurrentHashMap.values()) {
            // Point at which I decided to just do thread-per-connection lol.
            assert true;
            System.out.println(endpoint.getUniqueID().toString());
        }
    }

    public void startBroadcasting() {
        isBroadcasting = true;
    }
}
