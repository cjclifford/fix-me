package za.co.wethinkcode.fix_me;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class BrokerSocketHandler extends SocketHandler implements Runnable {
	
	BrokerSocketHandler(Socket socket, AMessageResponsibility messageHandler) throws OutOfIDSpaceException {
		super(socket, messageHandler);
	}

	@Override
	public void run() {
		System.out.println("Broker(" + this.socketId + ") connected");
		try {
			// get I/O for socket connection
			BufferedReader fromBroker = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			DataOutputStream toBroker = new DataOutputStream(this.socket.getOutputStream());

			// send ID to client
			System.out.println("Sending ID to broker...");
			toBroker.writeBytes(this.socketId + '\n');
			// listen for client message
			System.out.println("Waiting for broker message...");
			// validate message --> determine destination --> forwared message
//			this.messageHandler.handleRequest(new FixMessage(fromBroker.readLine()));
			// get message from Broker
			String message = fromBroker.readLine();
			System.out.println("Message from Broker:" + message);
			// determine destination
			String destinationId = message.split("\\|")[1].split("=")[1];
			System.out.println("Destination ID: " + destinationId);
			// forward message
			Socket destinationSocket = RoutingTable.getRoute(destinationId);
			DataOutputStream toDestination = new DataOutputStream(destinationSocket.getOutputStream());
			toDestination.writeBytes(message + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
