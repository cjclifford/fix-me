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
	BrokerSocketHandler(Socket socket, RoutingTable routingTable, int socketId) {
		super(socket, routingTable, socketId);
	}

	private Socket forwardMessage(String message) {
		String[] tags = message.split("\\|");
		Map<String, String> tagMap = new HashMap<String, String>();

		System.out.println("Extracting tags...");
		for (String tag : tags) {
			System.out.println(tag);
			String[] tagKV = tag.split("=");
			tagMap.put(tagKV[0], tagKV[1]);
		}
		
		try {			
			int marketId = Integer.parseInt(tagMap.get("MKT"));
			return this.routingTable.forward(marketId, message);
		} catch (NumberFormatException e) {
			return null;
		}
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
			if (this.validateChecksum(message)) {
				Socket market = this.forwardMessage(message);
				if (market == null)
					return;
				try {
					BufferedReader fromMarket = new BufferedReader(new InputStreamReader(market.getInputStream()));
					String response = fromMarket.readLine();
					if (!this.validateChecksum(response))
						return;
					toBroker.writeBytes(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			this.closeSocket();
		} catch (SocketTimeoutException e) {
			this.closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.closeSocket();
		}
	}
}
