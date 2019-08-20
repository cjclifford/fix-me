package za.co.wethinkcode.fix_me;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class BrokerServerSocketHandler implements Runnable {
	private int port = 5000;
	private ServerSocket serverSocket;
	private RoutingTable routingTable;
	private AMessageResponsibility messageHandler;

	BrokerServerSocketHandler(RoutingTable routingTable, AMessageResponsibility messageHandler) {
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		this.routingTable = routingTable;
		this.messageHandler = messageHandler;
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
					if (socket.getLocalPort() != this.port)
						return;
					socketHandler = new BrokerSocketHandler(socket, this.routingTable, socketId, this.messageHandler);
					new Thread(socketHandler).start();
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