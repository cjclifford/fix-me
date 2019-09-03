package za.co.wethinkcode.fix_me;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

public class BrokerSocketHandler extends SocketHandler implements Runnable {

	BrokerSocketHandler(Socket socket) throws OutOfIDSpaceException {
		super(socket);
	}

	@Override
	public void run() {
		System.out.println("Broker(" + this.socketId + ") connected");
		try {
			// get I/O for socket connection
			BufferedReader fromBroker = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			DataOutputStream toBroker = new DataOutputStream(this.socket.getOutputStream());

			// send ID to client
			toBroker.writeBytes(this.socketId + '\n');

			// get message from Broker
			String message = fromBroker.readLine();
			System.out.println("Message from Broker:" + message);

			Map<String, String> messageData = this.extractMessageData(message);

			// validate checksum
			if (!this.validateMessageChecksum(message)) {
				this.closeSocket();
				toBroker.writeBytes("Bad checksum\n");
				return;
			}

			// determine destination
			String destinationId = messageData.get("DST");

			// forward message
			Socket destinationSocket = RoutingTable.getRoute(destinationId);
			DataOutputStream toDestination = new DataOutputStream(destinationSocket.getOutputStream());
			toDestination.writeBytes(message + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
