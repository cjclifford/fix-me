package za.co.wethinkcode.fix_me;

import java.net.Socket;

public class SocketHandler {
	protected Socket socket;
	protected AMessageResponsibility messageHandler;
	protected String socketId;
	
	SocketHandler(Socket socket, AMessageResponsibility messageHandler) throws OutOfIDSpaceException {
		this.socket = socket;
		this.socketId = RoutingTable.addRoute(this.socket);
		this.messageHandler = messageHandler;
	}
}
