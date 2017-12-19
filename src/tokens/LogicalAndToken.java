package tokens;

public class LogicalAndToken implements Token {
	private LogicalAndToken() {}
	
	private static LogicalAndToken instance = new LogicalAndToken();
	
	public static LogicalAndToken getInstance() {
		return instance;
	}
}
