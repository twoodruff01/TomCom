A distributed messaging app, only for command-line use.

Each client can run ClientMain. This starts two threads:
- ClientServer
- ClientEndpointManager

The ClientServer just listens for new connections, wraps an Endpoint around them, and passes them to the
ClientEndpointManager

- An Endpoint is a class which wraps around the socket and communications between each client.
Each Endpoint is a thread, so this is a thread-per-connection model.

Whilst the ClientEndpointManager takes up an extra thread, it provides the option of refactoring this project
to not use a thread-per-connection model later on.

Might also add a master-server later which keeps track of all running clients, but can't really be bothered 
at this point as it'd be a lot more work.
-------------------------------------------------------------------------------------------------------------
I originally planned to not use a thread-per-connection model until I read this
https://dzone.com/articles/thousands-of-socket-connections-in-java-practical and was
reminded of how difficult that can be.

The ClientEndpointManager was originally intended to manage all the connections (Endpoints) for a client.
A cool project would be to actually implement this as I originally intended.