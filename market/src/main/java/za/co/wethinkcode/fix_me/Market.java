package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.io.DataOutputStream;
import java.io.IOException;

public class Market {
    public static void main( String[] args ) {
    	try {
    		System.out.println("Attempting connection to Router...");
    		Socket clientSocket = new Socket("127.0.0.1", 5001);
    		BufferedReader fromRouter = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		DataOutputStream toRouter = new DataOutputStream(clientSocket.getOutputStream());
    		
    		// get ID from router
    		String id = fromRouter.readLine();
    		System.out.println("ID: " + id);
    		
    		try {
    			MessageDigest md = MessageDigest.getInstance("MD5");
    			// listen for messages from router
    			while (true) {
    				String message = fromRouter.readLine();
    				if (message == null)
    					break;
    				System.out.println("Message from Broker: " + message);
    				// determine ID
    				String destinationId = message.split("\\|")[0].split("=")[1];
    				System.out.println("Destination ID: " + destinationId);
    				// add destination to response
    				String response = "ID=" + id + "|RES=Rejected" + "|DST=" + destinationId;
    				// generate checksum
    				md.update(response.getBytes());
    				String checksum = new BigInteger(1, md.digest()).toString(16);
    				// send response
    				toRouter.writeBytes(response + "|CHK=" + checksum + '\n');
    			}
    		} catch (NoSuchAlgorithmException e) {
    			e.printStackTrace();
    		}
    		fromRouter.close();
    		toRouter.close();
    		clientSocket.close();
    		System.out.println("Disconnected from Router");
    	} catch (IOException e) {
    		System.out.println(e);
    	}
    }
}