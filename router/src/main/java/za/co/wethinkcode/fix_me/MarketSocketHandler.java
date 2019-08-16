package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class MarketSocketHandler extends SocketHandler implements Runnable {
	MarketSocketHandler(Socket socket, RoutingTable routingTable, int socketId) {
		super(socket, routingTable, socketId);
	}
	
	private void forwardMessage(String message) {
		
	}
	
	public void run() {
		try {			
			BufferedReader fromMarket = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			DataOutputStream toBroker = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
