package za.co.wethinkcode.fix_me;

public class BrokerSimulation {
	public static void main(String[] args) {
		Broker broker = new Broker();

		broker.run(args[0], args[1], args[2], args[3], args[4]);
	}
}
