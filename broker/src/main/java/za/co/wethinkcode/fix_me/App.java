package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class App {
    public static void main( String[] args ) {
    	try {
    		System.out.println("Attempting connection on port...");
    		Socket clientSocket = new Socket("127.0.0.1", 5000);
    		BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
    		BufferedReader fromRouter = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		DataOutputStream toRouter = new DataOutputStream(clientSocket.getOutputStream());
    		
    		String input;
    		while (true) {
    			input = fromConsole.readLine();
    			if (input.toLowerCase().equals("quit"))
    				break;
//    			toRouter.writeBytes(input + '\n');
    		}
    		fromRouter.close();
    		clientSocket.close();
    	} catch (IOException e) {
    		System.out.println(e);
    	}
    }
}
