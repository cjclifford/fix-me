package za.co.wethinkcode.fix_me;

import java.lang.Thread;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Router {
	public static Map<String, Integer> routingTable = Collections.synchronizedMap(new HashMap<String, Integer>());

	public static void main(String[] args) {
		RoutingTable routingTable = new RoutingTable();

		Runnable brokerServerSocketHandler = new ServerSocketHandler(5000, routingTable);
		Runnable marketServerSocketHandler = new ServerSocketHandler(5001, routingTable);

		new Thread(brokerServerSocketHandler).start();
		new Thread(marketServerSocketHandler).start();
	}
}



class OutOfIDSpaceException extends Exception {
	public OutOfIDSpaceException(String errorMessage) {
		super(errorMessage);
	}
}
