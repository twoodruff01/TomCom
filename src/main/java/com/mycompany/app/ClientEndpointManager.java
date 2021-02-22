package com.mycompany.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
Keeps track of every Endpoint / connection that this client has.
- Should listen for messages from every client constantly and print them out
- Should be able to send message to everyone this client is connected to
 */
public class ClientEndpointManager extends Thread{
    private final ConcurrentHashMap<UUID, Endpoint> concurrentHashMap;

    public ClientEndpointManager() {
        concurrentHashMap = new ConcurrentHashMap<>();
        this.start();
    }

    public void addEndpoint(Endpoint endpoint) {
        System.out.println("Adding endpoint to ClientEndpointManager");
        concurrentHashMap.put(endpoint.getUniqueID(), endpoint);
    }

    @Override
    public void run() {

        while (!isInterrupted()) {  // Appropriate guard?
            for (Endpoint endpoint : concurrentHashMap.values()) {
                // Point at which I decided to just do thread-per-connection lol.
            }
        }
        super.run();
    }
}
