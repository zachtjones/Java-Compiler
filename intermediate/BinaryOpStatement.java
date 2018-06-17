package intermediate;

/** dest = src1 OP src2 */
public class BinaryOpStatement implements InterStatement {
	public static final char ADD = '+';
	public static final char SUB = '-';
	public static final char AND = '&';
	public static final char OR = '|';
	public static final char XOR = '^';
	public static final char DIV = '/';
	public static final char MOD = '%';
	public static final char TIMES = '*';
	public static final int LSHIFT = 0;
	public static final int RSHIFTARITH = 1; // sign-filled
	public static final int RSHIFTLOG = 2; // 0 filled.
	
	Register src1;
	Register src2;
	Register dest;
	int type;
	
	/** Creates an addition statement */
	public BinaryOpStatement(Register src1, Register src2, Register dest, char type) {
		this.src1 = src1;
		this.src2 = src2;
		this.dest = dest;
		this.type = type;
	}
	
	@Override
	public String toString() {
		if (type < 10) {
			if (type == LSHIFT) {
				return dest.toString() + " = " + src1.toString() + " << " + src2.toString() + ";";
			} else if (type == RSHIFTARITH) {
				return dest.toString() + " = " + src1.toString() + " >> " + src2.toString() + ";";
			} else {
				return dest.toString() + " = " + src1.toString() + " >>> " + src2.toString() + ";";
			}
		} else { // single char representation
			return dest.toString() + " = " + src1.toString() + " " 
				+ Character.toString( (char)type ) + " " + src2.toString() + ";";

		}
	}
}
