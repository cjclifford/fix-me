package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class MarketSocketHandler extends SocketHandler implements Runnable {

	MarketSocketHandler(Socket socket, AMessageResponsibility messageHandler) throws OutOfIDSpaceException{
		super(socket, messageHandler);
	}

	@Override
	public void run() {
		System.out.println("Market(" + this.socketId + ") connected");
		try {
			// get I/O for socket connection
			BufferedReader fromMarket = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			DataOutputStream toMarket = new DataOutputStream(this.socket.getOutputStream());

			// send ID to client
			System.out.println("Sending ID to market...");
			toMarket.writeBytes(this.socketId + '\n');
			// listen for client messages
			while (!this.socket.isClosed()) {
				System.out.println("Waiting for market message...");
				// validate message --> determine destination --> forward message
//				this.messageHandler.handleRequest(new FixMessage(fromMarket.readLine()));
				// get message from Market
				String message = fromMarket.readLine();
				System.out.println("Message from Market: " + message);
				// determine destination
				String destinationId = message.split("\\|")[2].split("=")[1];
				System.out.println("Destination ID: " + destinationId);
				// forward message
				Socket destinationSocket = RoutingTable.getRoute(destinationId);
				DataOutputStream toDestination = new DataOutputStream(destinationSocket.getOutputStream());
				toDestination.writeBytes(message + '\n');
				// close Broker socket
				destinationSocket.close();
				RoutingTable.removeRoute(destinationId);
				System.out.println("Broker disconnected");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.socket.close();
				RoutingTable.removeRoute(this.socketId);
				System.out.println("Market(" + this.socketId + ") disconnected");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
