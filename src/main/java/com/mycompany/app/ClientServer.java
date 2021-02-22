package com.mycompany.app;

/*
- Listens for connections from other clients
- When it gets a connections:
    wrap an endpoint around that connection
    pass that endpoint to EndpointManager (each client should only have one EndpointManager)
- Repeat above indefinitely...
 */
public class ClientServer extends Thread {

    public ClientServer(String hostName, int port) {


        this.start();
    }


    @Override
    public void run() {


        super.run();
    }
}
