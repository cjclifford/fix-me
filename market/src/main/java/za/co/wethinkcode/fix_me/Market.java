package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class Market {
    public static void main( String[] args ) {
    	try {
    		System.out.println("Attempting connection on port...");
    		Socket clientSocket = new Socket("127.0.0.1", 5001);
    		BufferedReader fromBroker = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		while (true) {
    			System.out.println("Message from broker: " + fromBroker.readLine());
    		}
//    		clientSocket.close();
    	} catch (IOException e) {
    		System.out.println(e);
    	}
    }
}