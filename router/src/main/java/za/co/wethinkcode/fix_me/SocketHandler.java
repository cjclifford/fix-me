package za.co.wethinkcode.fix_me;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class SocketHandler {
	protected Socket socket;
	protected RoutingTable routingTable;
	protected int socketId;
	
	SocketHandler(Socket socket, RoutingTable routingTable, int socketId) {
		this.socket = socket;
		this.routingTable = routingTable;
		this.socketId = socketId;
	}
	
	protected void closeSocket() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected boolean validateChecksum(String message) {
		String checksum = message.substring(message.lastIndexOf("=") + 1);
		String rawMessage = message.substring(0, message.lastIndexOf("|"));
		System.out.println("Raw message: " + rawMessage);
		try {			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(rawMessage.getBytes());
			String hash = new BigInteger(1, md.digest()).toString(16);
			return checksum.equals(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
}
