package za.co.wethinkcode.fix_me;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarketServerSocketHandler implements Runnable {
	private final int port = 5001;
	ServerSocket serverSocket;
	
	@Override
	public void run() {
		System.out.println("Listening for Market connections...");
		try {			
			this.serverSocket = new ServerSocket(this.port);
			ExecutorService executor = Executors.newFixedThreadPool(5);
			while (true) {
				try {
					executor.execute(new MarketSocketHandler(serverSocket.accept()));
				} catch (OutOfIDSpaceException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.serverSocket.close();
				System.out.println("Stopped listening for Market connections");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
