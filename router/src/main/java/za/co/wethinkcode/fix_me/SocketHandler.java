package za.co.wethinkcode.fix_me;

import java.net.Socket;

public class SocketHandler {
	protected synchronized Socket socket;
	protected RoutingTable routingTable;
	protected AMessageResponsibility messageHandler;
	protected int socketId;
	
	SocketHandler(Socket socket, RoutingTable routingTable, int socketId) {
		this.socket = socket;
		this.routingTable = routingTable;
		this.socketId = socketId;
	}
}
