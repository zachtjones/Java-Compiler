package tokens;
/** This is the >>> operator */
public class RightShift0Extend implements Token {
	private RightShift0Extend() {}
	
	private static RightShift0Extend instance = new RightShift0Extend();
	
	public static RightShift0Extend getInstance() {
		return instance;
	}
}
