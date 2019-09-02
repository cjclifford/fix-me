package za.co.wethinkcode.fix_me;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BrokerServerSocketHandler implements Runnable {
	private final int port = 5000;
	private AMessageResponsibility messageHandler;
	ServerSocket serverSocket;

	BrokerServerSocketHandler(AMessageResponsibility messageHandler) {
		this.messageHandler = messageHandler;
	}
	
	@Override
	public void run() {
		System.out.println("Listening for Broker connections...");
		try {
			this.serverSocket = new ServerSocket(this.port);
			ExecutorService executor = Executors.newFixedThreadPool(5);
			while (true) {
				try {					
					executor.execute(new BrokerSocketHandler(serverSocket.accept(), messageHandler));
				} catch (OutOfIDSpaceException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.serverSocket.close();
				System.out.println("Stopped listening for Broker connections");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}