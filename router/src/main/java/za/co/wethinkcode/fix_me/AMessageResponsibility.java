package za.co.wethinkcode.fix_me;

public abstract class AMessageResponsibility {
	protected AMessageResponsibility nextHandler;
	
	public void setNextHandler(AMessageResponsibility nextHandler) {
		this.nextHandler = nextHandler;
	}
	
	public abstract boolean handleRequest(FixMessage fixMessage);
}
