package intermediate;

public class Register {
	
	public static final int BYTE = 0;
	public static final int SHORT = 1;
	public static final int INT = 2;
	public static final int LONG = 3;
	public static final int FLOAT = 4;
	public static final int DOUBLE = 5;
	public static final int REFERENCE = 6;
	
	
	int num;
	int type;
	
	public Register(int num, int type) {
		this.num = num;
		this.type = type;
	}
}
