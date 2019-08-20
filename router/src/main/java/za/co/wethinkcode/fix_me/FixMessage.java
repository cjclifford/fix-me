package za.co.wethinkcode.fix_me;

import java.util.Map;
import java.util.HashMap;

public class FixMessage {
	public String message;
	public int destinationId;
	public Map<String, String> tags;
	
	public FixMessage(String message) {
		this.message = message;
		this.tags = this.parseMessage(this.message);
		try {			
			this.destinationId = Integer.parseInt(this.tags.get("DST"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, String> parseMessage(String message) {
		String[] tags = message.split("\\|");
		Map<String, String> tagMap = new HashMap<String, String>();
		
		for (String tag : tags) {
			System.out.println(tag);
			String[] tagKV = tag.split("=");
			tagMap.put(tagKV[0], tagKV[1]);
		}
		return tagMap;
	}
}
