package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class MarketSocketHandler extends SocketHandler implements Runnable {
	AMessageResponsibility messageHandler;
	
	MarketSocketHandler(Socket socket, RoutingTable routingTable, int socketId, AMessageResponsibility messageHandler) {
		super(socket, routingTable, socketId);
		this.messageHandler = messageHandler;
	}
	
	public void run() {
		try {			
			BufferedReader fromMarket = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			DataOutputStream toMarket = new DataOutputStream(this.socket.getOutputStream());
			toMarket.writeBytes(Integer.toString(this.socketId) + '\n');
			System.out.println("Connected to market");
			System.out.println("Waiting for market message...");
			String message = fromMarket.readLine();
			System.out.println("Broker message: " + message);
			FixMessage fixMessage = new FixMessage(message);
			this.messageHandler.handleRequest(fixMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
