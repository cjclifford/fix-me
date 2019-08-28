package za.co.wethinkcode.fix_me;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ForwardMessageHandler extends AMessageResponsibility {
	private RoutingTable routingTable;

	public ForwardMessageHandler(RoutingTable routingTable) {
		this.routingTable = routingTable;
	}
	
	public boolean handleRequest(FixMessage fixMessage) {
		Socket socket = this.routingTable.getRoute(fixMessage.destinationId);
		try {
			DataOutputStream sendTo = new DataOutputStream(socket.getOutputStream());
			sendTo.writeBytes(fixMessage.message);
			sendTo.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
