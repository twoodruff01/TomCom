package com.mycompany.app;

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
public class Endpoint {
    private final Socket socket;
    private final UUID uniqueID;

    public Endpoint(Socket socket) {
        this.socket = socket;
        uniqueID = UUID.randomUUID();
    }

    public UUID getUniqueID() {
        return uniqueID;
    }
}
