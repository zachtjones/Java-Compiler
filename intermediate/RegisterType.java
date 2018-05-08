package intermediate;

public class RegisterType {
	public final static int BYTE = 0;
	public final static int SHORT = 1;
	public final static int INT = 2;
	public final static int LONG = 3;
	public final static int FLOAT = 4;
	public final static int DOUBLE = 5;
	public final static int REFERENCE = 6;
	
	public static String toString(int type) {
		switch(type) {
		case BYTE: return "%b";
		case SHORT: return "%s";
		case INT: return "%i";
		case LONG: return "%l";
		case FLOAT: return "%f";
		case DOUBLE: return "%d";
		default:
			return "%r";
		}
	}
}
