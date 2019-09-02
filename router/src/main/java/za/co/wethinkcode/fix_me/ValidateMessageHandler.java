package za.co.wethinkcode.fix_me;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ValidateMessageHandler extends AMessageResponsibility {
	public boolean handleRequest(FixMessage fixMessage) {
		String message = fixMessage.message;
		String checksum = message.substring(message.lastIndexOf("=") + 1);
		String rawMessage = message.substring(0, message.lastIndexOf("|"));
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(rawMessage.getBytes());
			String hash = new BigInteger(1, md.digest()).toString(16);
			if (checksum.equals(hash) && this.nextHandler != null) {				
				if (this.nextHandler == null)
					return true;
				return this.nextHandler.handleRequest(fixMessage);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

}
