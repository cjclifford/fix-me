package za.co.wethinkcode.fix_me;

public class GetMessageDestinationHandler extends AMessageResponsibility {
	public boolean handleRequest(FixMessage fixMessage) {
		String destinationId = fixMessage.tags.get("DST");
		int tempDestinationId = Integer.parseInt(destinationId);
		if (tempDestinationId < 100000 || tempDestinationId > 999999)
			return false;
		fixMessage.destinationId = destinationId;
		if (this.nextHandler == null)
			return true;
		return this.nextHandler.handleRequest(fixMessage);
	}
}
