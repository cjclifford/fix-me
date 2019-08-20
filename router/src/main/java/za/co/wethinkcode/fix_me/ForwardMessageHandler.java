package za.co.wethinkcode.fix_me;

public class ForwardMessageHandler extends AMessageResponsibility {
	private RoutingTable routingTable;

	public ForwardMessageHandler(RoutingTable routingTable) {
		this.routingTable = routingTable;
	}
	
	public boolean handleRequest(FixMessage fixMessage) {
		return this.routingTable.forward(fixMessage.destinationId, fixMessage.message);
	}
}
