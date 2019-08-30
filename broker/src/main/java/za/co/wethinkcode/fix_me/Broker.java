package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class Broker {
    public static void main( String[] args ) {
    	try {
    		System.out.println("Attempting connection to Router...");
    		Socket clientSocket = new Socket("127.0.0.1", 5000);
    		BufferedReader fromRouter = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		DataOutputStream toRouter = new DataOutputStream(clientSocket.getOutputStream());
    		
    		// get ID from router
    		String id = fromRouter.readLine();
    		System.out.println("ID: " + id);
    		
    		String message = "ID=" + id + "|DST=" + args[0];
    		try {
    			// generate checksum
    			MessageDigest md = MessageDigest.getInstance("MD5");
    			md.update(message.getBytes());
    			String checksum = new BigInteger(1, md.digest()).toString(16);
    			// append checksum to message
    			message += "|CHK=" + checksum;
    			// send message
    			System.out.println("Sending message: " + message);
    			toRouter.writeBytes(message + '\n');
    			// get response from router
    			String response = fromRouter.readLine();
    			System.out.println("Response: " + response);
    		} catch (NoSuchAlgorithmException e) {
    			e.printStackTrace();
    		}
    		fromRouter.close();
    		clientSocket.close();
    		clientSocket.close();
    		System.out.println("Disconnected from Router");
    	} catch (IOException e) {
    		System.out.println(e);
    	}    		
    }
}
