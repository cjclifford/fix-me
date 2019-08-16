package za.co.wethinkcode.fix_me;

import java.io.DataOutputStream;
import java.io.IOException;
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

	public void removeRoute(int id) {
		this.routingTable.remove(id);
	}
	
	public Socket forward(int id, String message) {
		Socket socket = this.routingTable.get(id);
		try {
			DataOutputStream sendTo = new DataOutputStream(socket.getOutputStream());
			sendTo.writeBytes(message);
			sendTo.close();
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
