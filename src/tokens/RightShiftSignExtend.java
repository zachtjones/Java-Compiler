package tokens;

public class RightShiftSignExtend implements Token {
	private RightShiftSignExtend() {}
	
	private static RightShiftSignExtend instance = new RightShiftSignExtend();
	
	public static RightShiftSignExtend getInstance() {
		return instance;
	}
}
