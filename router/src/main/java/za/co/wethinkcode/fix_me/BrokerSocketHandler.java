package za.co.wethinkcode.fix_me;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.HashMap;

public class BrokerSocketHandler extends SocketHandler implements Runnable {
	private AMessageResponsibility messageHandler;
	
	BrokerSocketHandler(Socket socket, RoutingTable routingTable, int socketId, AMessageResponsibility messageHandler) {
		super(socket, routingTable, socketId);
		this.messageHandler = messageHandler;
	}

	public void run() {
		System.out.println("Socket connection through port: " + this.socket.getLocalPort());
		try {
			BufferedReader fromBroker = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			DataOutputStream toBroker = new DataOutputStream(this.socket.getOutputStream());
			toBroker.writeBytes(Integer.toString(this.socketId) + '\n');
			System.out.println("Connected to broker");
			System.out.println("Waiting for broker message...");
			String message = fromBroker.readLine();
			System.out.println("Broker message: " + message);
			FixMessage fixMessage = new FixMessage(message);
			this.messageHandler.handleRequest(fixMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
