package tokens;

public class LessThanEqualToken implements Token {
	private LessThanEqualToken() {}
	
	private static LessThanEqualToken instance = new LessThanEqualToken();
	
	public static LessThanEqualToken getInstance() {
		return instance;
	}
}
