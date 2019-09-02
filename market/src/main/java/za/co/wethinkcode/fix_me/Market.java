package za.co.wethinkcode.fix_me;

import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.io.DataOutputStream;
import java.io.IOException;

public class Market {
	public static void main(String[] args) {
		AResponsibility messageHandler = new BuyHandler();
		messageHandler.setNextHandler(new SellHandler());
		Map<String, Float> instruments = new HashMap<String, Float>();

		// add some mock instruments
		instruments.put("H", new Float(10.0));
		try {

			System.out.println("Attempting connection to Router...");
			Socket clientSocket = new Socket("127.0.0.1", 5001);
			BufferedReader fromRouter = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			DataOutputStream toRouter = new DataOutputStream(clientSocket.getOutputStream());

			// get ID from router
			String id = fromRouter.readLine();
			System.out.println("ID: " + id);

			try {
				MessageDigest md = MessageDigest.getInstance("MD5");

				// listen for messages from router
				while (true) {
					String message = fromRouter.readLine();
					if (message == null)
						break;
					System.out.println("Message from Broker: " + message);

					Map<String, String> messageData = extractMessageData(message);

					// determine ID
					String destinationId = messageData.get("ID");
					System.out.println("Destination ID: " + destinationId);

					boolean success = false;

					String instrument = messageData.get("IST");
					String amount = messageData.get("AMT");
					String price = messageData.get("PRC");

					if (instrument != null && instruments.containsKey(instrument) && amount != null && price != null) {
						// CoR
						try {
							success = messageHandler.handle(messageData.get("TYP"), instruments.get(instrument),
									Integer.parseInt(amount), Float.parseFloat(price));
						} catch (NumberFormatException e) {
							success = false;
						}
					}

					// generate response
					String response = "ID=" + id + "|RES=" + (success ? "Accepted" : "Rejected") + "|DST="
							+ destinationId;

					// generate checksum
					md.update(response.getBytes());
					String checksum = new BigInteger(1, md.digest()).toString(16);

					// send response
					toRouter.writeBytes(response + "|CHK=" + checksum + '\n');
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			fromRouter.close();
			toRouter.close();
			clientSocket.close();
			System.out.println("Disconnected from Router");
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static Map<String, String> extractMessageData(String message) {
		Map<String, String> messageData = new HashMap<String, String>();
		String[] rawMessageTags = message.split("\\|");
		for (int i = 0; i < rawMessageTags.length; i++) {
			String[] pair = rawMessageTags[i].split("=");
			messageData.put(pair[0], pair[1]);
		}
		return messageData;
	}
}