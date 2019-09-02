package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class MarketSocketHandler extends SocketHandler implements Runnable {

	MarketSocketHandler(Socket socket) throws OutOfIDSpaceException{
		super(socket);
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
				
				// get message from Market
				String message = fromMarket.readLine();
				System.out.println("Message from Market: " + message);
				
				// validate checksum
				if (!this.validateMessageChecksum(message)) {
					this.closeSocket();
					toMarket.writeBytes("Bad checksum\n");
					return;
				}
				
				Map<String, String> messageData = this.extractMessageData(message);
				
				// determine destination
				String destinationId = messageData.get("DST");
				System.out.println("Destination ID: " + destinationId);
				
				// forward message
				Socket destinationSocket = RoutingTable.getRoute(destinationId);
				DataOutputStream toDestination = new DataOutputStream(destinationSocket.getOutputStream());
				toDestination.writeBytes(message + '\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.closeSocket();
			System.out.println("Market(" + this.socketId + ") disconected");
		}
	}
}
