package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Thread;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class App {
	public static Map<String, Integer> routingTable = Collections.synchronizedMap(new HashMap<String, Integer>());

	public static void main(String[] args) {
		RoutingTable routingTable = new RoutingTable();

		Runnable brokerServerSocketHandler = new ServerSocketHandler(5000, routingTable);
		Runnable marketServerSocketHandler = new ServerSocketHandler(5001, routingTable);
		
		new Thread(brokerServerSocketHandler).start();
		new Thread(marketServerSocketHandler).start();
	}
}

class RoutingTable {
	private Map<Integer, Socket> routingTable;
	private int nextId = 100000;
	
	RoutingTable() {
		this.routingTable = new HashMap<Integer, Socket>();
	}
	
	public int addRoute(Socket socket) throws OutOfIDSpaceException {
		if (socket != null) {
			if (this.nextId > 999999 || this.routingTable.get(this.nextId) != null) {
				for (int i = 100000; i <= 999999; i++) {
					if (this.routingTable.get(i) == null)
						this.nextId = i;
				}
				throw new OutOfIDSpaceException("No more routes could be established at this time.");
			}		
			this.routingTable.put(this.nextId, socket);
		}
		return this.nextId++;
	}

	public void removeRoute(int id) {
		this.routingTable.remove(id);
	}
}

class ServerSocketHandler implements Runnable {
	private ServerSocket serverSocket;
	private RoutingTable routingTable;
	
	ServerSocketHandler(int port, RoutingTable routingTable) {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		this.routingTable = routingTable;
	}

	public void run() {
		System.out.println("Listening...");
		while (true) {
			try {
				Socket socket = this.serverSocket.accept();
				socket.setSoTimeout(10*1000);
				try {					
					int socketId = this.routingTable.addRoute(socket);
					Runnable socketHandler = new SocketHandler(socket, this.routingTable, socketId);
					new Thread(socketHandler).start();
				} catch (OutOfIDSpaceException e) {
					e.printStackTrace();
					socket.close();
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class SocketHandler implements Runnable {
	private Socket socket;
	private RoutingTable routingTable;
	private int socketId;
	
	SocketHandler(Socket socket, RoutingTable routingTable, int socketId) {
		this.socket = socket;
		this.routingTable = routingTable;
		this.socketId = socketId;
	}
	
	private void closeSocket() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		try {			
			DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
			out.writeBytes(message + '\n');
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		System.out.println("Socket connection through port: " + this.socket.getLocalPort());
		try {
			BufferedReader fromBroker = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			DataOutputStream toBroker = new DataOutputStream(this.socket.getOutputStream());
			toBroker.writeBytes(Integer.toString(this.socketId) + '\n');
			switch (this.socket.getLocalPort()) {
			case 5000:
				System.out.println("Connected to broker");
				System.out.println("Waiting for broker message...");
				String message = fromBroker.readLine();
				System.out.println("Broker message: " + message);
				// handle message here
			case 5001:
				System.out.println("Connected to market");
			}
		} catch (SocketException e) {
			this.closeSocket();
		} catch (SocketTimeoutException e) {
			this.closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.closeSocket();
		}
	}
}

class OutOfIDSpaceException extends Exception {
	public OutOfIDSpaceException(String errorMessage) {
		super(errorMessage);
	}
}
