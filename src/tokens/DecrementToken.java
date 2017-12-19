package tokens;

public class DecrementToken implements Token {
	private DecrementToken() {}
	
	private static DecrementToken instance = new DecrementToken();
	
	public static DecrementToken getInstance() {
		return instance;
	}
}
