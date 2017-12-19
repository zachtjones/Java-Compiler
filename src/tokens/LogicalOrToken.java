package tokens;

public class LogicalOrToken implements Token {
	private LogicalOrToken() {}
	
	private static LogicalOrToken instance = new LogicalOrToken();
	
	public static LogicalOrToken getInstance() {
		return instance;
	}
}
