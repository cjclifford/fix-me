package za.co.wethinkcode.fix_me;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SocketHandler {
	protected Socket socket;
	protected String socketId;
	
	SocketHandler(Socket socket) throws OutOfIDSpaceException {
		this.socket = socket;
		this.socketId = RoutingTable.addRoute(this.socket);
	}
	
	protected Map<String, String> extractMessageData(String message) {
		Map<String, String> messageData = new HashMap<String, String>();
		String[] rawMessageTags = message.split("\\|");
		for (int i = 0; i < rawMessageTags.length; i++) {
			String[] pair = rawMessageTags[i].split("=");
			messageData.put(pair[0], pair[1]);
		}
		return messageData;
	}
	
	protected boolean validateMessageChecksum(String message) {
		String messageBody = message.substring(0, message.lastIndexOf("|"));
		String checksum = message.substring(message.lastIndexOf("=") + 1);
		try {			
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(messageBody.getBytes());
			String hash = new BigInteger(1, digest.digest()).toString(16);
			return hash.equals(checksum);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected void closeSocket() {
		try {
			this.socket.close();
			RoutingTable.removeRoute(this.socketId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
