package za.co.wethinkcode.fix_me;

public class BuyHandler extends AResponsibility {
	public boolean handle(String requestType, float localPrice, int amount, float price) {
		if (requestType.equals("BUY") && amount > 0 && price == localPrice)
			return true;
		if (this.nextHandler == null)
			return false;
		return this.nextHandler.handle(requestType, localPrice, amount, price);
	}
}
