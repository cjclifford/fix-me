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
    		System.out.println("Attempting connection on port...");
    		Socket clientSocket = new Socket("127.0.0.1", 5000);
    		BufferedReader fromRouter = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		DataOutputStream toRouter = new DataOutputStream(clientSocket.getOutputStream());
    		
    		int id = Integer.parseInt(fromRouter.readLine());
    		
    		String message = "ID=" + id + "|DST=" + 100000;
    		try {    			
    			MessageDigest md = MessageDigest.getInstance("MD5");
    			md.update(message.getBytes());
    			String checksum = new BigInteger(1, md.digest()).toString(16);
    			message += "|CHK=" + checksum;
    			System.out.println("Sending message: " + message);
    			toRouter.writeBytes(message);
    		} catch (NoSuchAlgorithmException e) {
    			e.printStackTrace();
    		}
    		fromRouter.close();
    		clientSocket.close();
    	} catch (IOException e) {
    		System.out.println(e);
    	}
    }
}
