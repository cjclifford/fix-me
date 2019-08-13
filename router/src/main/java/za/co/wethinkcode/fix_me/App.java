package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Thread;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
	public static Map<String, Integer> routingTable = Collections.synchronizedMap(new HashMap<String, Integer>());
	public static AtomicInteger lastId = new AtomicInteger(100000);

	public static void main(String[] args) {

		try {
			ServerSocket brokerServerSocket = new ServerSocket(5000);
			ServerSocket marketServerSocket = new ServerSocket(5001);
			new Thread(() -> threadListenBroker(brokerServerSocket)).start();
			new Thread(() -> threadListenMarket(marketServerSocket)).start();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void threadListenBroker(ServerSocket serverSocket) {
		System.out.println("Listening for connections...");
		while (true) {
			try {
				System.out.println("Creating connection thread...");
				Socket socket = serverSocket.accept();
				int id = lastId.get();
				String idString = Integer.toString(id);
				if (id > 999999)
					return;
				routingTable.put(Integer.toString(id), 0);
				lastId.set(id + 1);
				new Thread(() -> threadBroker(socket, idString)).start();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	public static void threadListenMarket(ServerSocket serverSocket) {
		System.out.println("Listening for connections...");
		while (true) {
			try {
				System.out.println("Creating connection thread...");
				Socket socket = serverSocket.accept();
				int id = lastId.get();
				String idString = Integer.toString(id);
				if (id > 999999)
					return;
				routingTable.put(Integer.toString(id), 0);
				lastId.set(id + 1);
				new Thread(() -> threadMarket(socket, idString)).start();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void threadBroker(Socket socket, String idString) {
		try {
			System.out.println(socket);
			// setup io stream for broker
			BufferedReader fromBroker = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream toBroker = new DataOutputStream(socket.getOutputStream());
			// send broker ID
			toBroker.writeBytes(idString);
			// read FIX message
			String message = fromBroker.readLine();
			// establish connection with market
			// forward message to market
			// get market response
			// send broker market response
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void threadMarket(Socket socket, String idString) {
		try {
			System.out.println(socket);
			// setup io stream for market
			// send market ID
			// read market response
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
