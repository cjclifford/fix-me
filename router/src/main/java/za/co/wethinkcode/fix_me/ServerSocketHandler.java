package za.co.wethinkcode.fix_me;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
				socket.setSoTimeout(10 * 1000);
				try {
					int socketId = this.routingTable.addRoute(socket);
					Runnable socketHandler;
					switch (socket.getLocalPort()) {
					case 5000:
						socketHandler = new BrokerSocketHandler(socket, this.routingTable, socketId);
						new Thread(socketHandler).start();
						break;
					case 5001:
						socketHandler = new MarketSocketHandler(socket, this.routingTable, socketId);
						new Thread(socketHandler).start();
					}
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