package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RoutingTable {
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
				throw new OutOfIDSpaceException("No more routes could be created at this time.");
			}
			this.routingTable.put(this.nextId, socket);
		}
		return this.nextId++;
	}
	
	public Socket getRoute(int id) {
		return this.routingTable.get(id);
	}

	public void removeRoute(int id) {
		this.routingTable.remove(id);
	}
}
