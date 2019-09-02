package za.co.wethinkcode.fix_me;

import java.lang.Thread;

public class Router {
	public static void main(String[] args) {
		Runnable brokerServerSocketHandler = new BrokerServerSocketHandler();
		Runnable marketServerSocketHandler = new MarketServerSocketHandler();

		new Thread(brokerServerSocketHandler).start();
		new Thread(marketServerSocketHandler).start();
	}
}

class OutOfIDSpaceException extends Exception {
	public OutOfIDSpaceException(String errorMessage) {
		super(errorMessage);
	}
}
