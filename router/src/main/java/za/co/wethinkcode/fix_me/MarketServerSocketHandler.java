package za.co.wethinkcode.fix_me;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MarketServerSocketHandler implements Runnable {
	final int port = 5001;
	private ServerSocket serverSocket;
	private RoutingTable routingTable;
	private AMessageResponsibility messageHandler;
	
	public MarketServerSocketHandler(RoutingTable routingTable, AMessageResponsibility messageHandler) {
		try {			
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.routingTable = routingTable;
		this.messageHandler = messageHandler;
	}

	public void run() {
		System.out.println("Listening for markets...");
		while (true) {
			try {
				Socket socket = this.serverSocket.accept();
				socket.setSoTimeout(10 * 1000);
				int socketId;
				try {
					socketId = this.routingTable.addRoute(socket);
					Runnable socketHandler;
					if (socket.getLocalPort() == this.port) {
						if (socket.getLocalPort() != this.port)
							return;
						socketHandler = new MarketSocketHandler(socket, this.routingTable, socketId, this.messageHandler);
						new Thread(socketHandler).start();
					}
				} catch (OutOfIDSpaceException e) {
					e.printStackTrace();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
