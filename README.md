A distributed messaging app, only for command-line use.

Each client can run ClientMain. This will start up two threads on their machine:
- ClientServer
- ClientEndpointManager

The ClientServer just waits for connections to the client and passes them to the ClientEndpointManager

The ClientEndpointManager manages all the connections (Endpoints) for a client.

An Endpoint is a class which wraps around the socket and communications between each client. I've decided not
to have Endpoint extend Thread, so this isn't a thread-per-connection model.

Might add a master-server later which keeps track of all running clients, but can't really be bothered at this 
point as it'd be a lot more work.

