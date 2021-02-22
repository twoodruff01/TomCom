package com.mycompany.app;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
Keeps track of every Endpoint / connection that this client has.
- Should listen for messages from every client constantly and print them out
- Should be able to send message to everyone this client is connected to
 */
public class ClientEndpointManager {
    private final ConcurrentHashMap<UUID, Endpoint> concurrentHashMap;


    public ClientEndpointManager() {
        concurrentHashMap = new ConcurrentHashMap<>();
    }

    public void addEndpoint(Endpoint endpoint) {
        System.out.println("Adding endpoint to ClientEndpointManager");
        concurrentHashMap.put(endpoint.getUniqueID(), endpoint);
    }

}
