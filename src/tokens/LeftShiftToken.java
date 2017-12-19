package tokens;

public class LeftShiftToken implements Token {
	private LeftShiftToken() {}
	
	private static LeftShiftToken instance = new LeftShiftToken();
	
	public static LeftShiftToken getInstance() {
		return instance;
	}
}
