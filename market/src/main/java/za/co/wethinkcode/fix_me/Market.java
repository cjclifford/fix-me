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
    	String id;
    	
    	try {
    		System.out.println("Attempting connection on port...");
    		Socket clientSocket = new Socket("127.0.0.1", 5001);
    		BufferedReader fromRouter = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		DataOutputStream toRouter = new DataOutputStream(clientSocket.getOutputStream());
    		
    		try {    			
    			MessageDigest md = MessageDigest.getInstance("MD5");
    			id = fromRouter.readLine();
    			System.out.println(fromRouter.readLine());
    			toRouter.writeBytes("hello\n");
    			System.out.println(id);
    			while (true) {
    				String message = fromRouter.readLine();
    				if (message == null)
    					break;
    				System.out.println("Message from broker: " + message);
    				String response = "ID=" + id + "|RES=Rejected";
    				md.update(response.getBytes());
    				String checksum = new BigInteger(1, md.digest()).toString(16);
    				toRouter.writeBytes(response + "|CHK=" + checksum + "\n");
    			}
    			clientSocket.close();
    			System.out.println("Socket closed");
    		} catch (NoSuchAlgorithmException e) {
    			e.printStackTrace();
    		}
    	} catch (IOException e) {
    		System.out.println(e);
    	}
    }
}