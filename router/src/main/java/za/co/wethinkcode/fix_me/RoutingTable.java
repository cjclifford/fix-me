package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public abstract class RoutingTable {
	private static Map<String, Socket> routingTable = new HashMap<String, Socket>();
	private static int nextId = 100000;

	public static String addRoute(Socket socket) throws OutOfIDSpaceException {
		if (socket != null) {
			if (nextId > 999999 || routingTable.get(Integer.toString(nextId)) != null) {
				for (int i = 100000; i <= 999999; i++) {
					if (routingTable.get(Integer.toString(i)) == null)
						nextId = i;
				}
				throw new OutOfIDSpaceException("No more routes could be created at this time.");
			}
			routingTable.put(Integer.toString(nextId), socket);
		}
		return Integer.toString(nextId++);
	}
	
	public static synchronized Socket getRoute(String id) {
		return routingTable.get(id);
	}

	public static synchronized void removeRoute(String id) {
		routingTable.remove(id);
	}
}
