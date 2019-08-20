package za.co.wethinkcode.fix_me;

public class GetMessageDestinationHandler extends AMessageResponsibility {
	public boolean handleRequest(FixMessage fixMessage) {
		int destinationId = Integer.parseInt(fixMessage.tags.get("DST"));
		if (destinationId < 100000 || destinationId > 999999)
			return false;
		fixMessage.destinationId = destinationId;
		if (this.nextHandler == null)
			return true;
		return this.nextHandler.handleRequest(fixMessage);
	}
}
