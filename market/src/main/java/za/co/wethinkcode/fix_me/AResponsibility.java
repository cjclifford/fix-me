package za.co.wethinkcode.fix_me;

public abstract class AResponsibility {
	public AResponsibility nextHandler;
	
	public abstract boolean handle(String requestType, float localPrice, int amount, float price);
	
	public void setNextHandler(AResponsibility nextHandler) {
		this.nextHandler = nextHandler;
	}
}
