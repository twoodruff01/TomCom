A distributed messaging app, only for command-line use.

I wrote this a few months after finishing the subject Distributed Systems at Melbourne, as a refresher 
on sockets, threads, and java. Though I wrote all of this, I learnt the patterns 
(especially for Endpoint.java) from my lecturer Aaron Harwood.

Each client can run ClientMain. This starts three threads:
1. ClientServer: accepts new endpoints
2. ClientEndpointManager: manages all endpoints
3. consoleListenerThread: handles console input (i.e. messaging)

What is an Endpoint? It's a class which wraps around the socket and communications between each client.
Each Endpoint is a thread, so this is a thread-per-connection model.

-------------------------------------------------------------------------------------------------------------

*How to run it*
- mvn package
- java -cp target/TomCom-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.ClientMain
- java -cp target/TomCom-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.ClientMain -lp 20001 -c -n Frank
- java -cp target/TomCom-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.ClientMain -lp 20002 -c -n Greg

This will start up three different clients. The first client can talk to both of the others simultaneously,
but the 2nd and 3rd clients can't talk to each other (only the server). So the server act's kind of like a
publisher and all the processes that connect are subscribing to it (though they can send messages to the
server individually).

If you can't get that to work, run:
- java -cp target/TomCom-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.ClientMain --help

-------------------------------------------------------------------------------------------------------------

Might add a master-server later which keeps track of all running clients so every client can talk to every
other client, but can't really be bothered at this point, as it'd be a lot more work.

-------------------------------------------------------------------------------------------------------------

I originally planned to not use a thread-per-connection model until I read this
https://dzone.com/articles/thousands-of-socket-connections-in-java-practical and realised that is far more
complicated than what I intended to build

A cool project would be to actually implement this without using a thread for every connection.

Have only tested it with localhost so far.