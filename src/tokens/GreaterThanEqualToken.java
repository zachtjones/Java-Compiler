package tokens;

public class GreaterThanEqualToken implements Token {
	private GreaterThanEqualToken() {}
	
	private static GreaterThanEqualToken instance = new GreaterThanEqualToken();
	
	public static GreaterThanEqualToken getInstance() {
		return instance;
	}
}
