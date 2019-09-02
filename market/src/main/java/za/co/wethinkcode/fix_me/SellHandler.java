package za.co.wethinkcode.fix_me;

public class SellHandler extends AResponsibility {
	public boolean handle(String requestType, float localPrice, int amount, float price) {
		if (requestType.equals("SELL") && amount > 0 && amount * price == amount * localPrice)
			return true;
		if (this.nextHandler == null)
			return false;
		return this.nextHandler.handle(requestType, localPrice, amount, price);
	}
}
