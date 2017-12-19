package tokens;

public class IncrementToken implements Token {
	private IncrementToken() {}
	
	private static IncrementToken instance = new IncrementToken();
	
	public static IncrementToken getInstance() {
		return instance;
	}
}
