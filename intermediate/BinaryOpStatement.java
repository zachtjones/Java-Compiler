package intermediate;

/** dest = src1 OP src2 */
public class BinaryOpStatement implements InterStatement {
	public static final char ADD = '+';
	public static final char AND = '&';
	public static final char OR = '|';
	public static final char XOR = '^';
	
	Register src1;
	Register src2;
	Register dest;
	char type;
	
	/** Creates an addition statement */
	public BinaryOpStatement(Register src1, Register src2, Register dest, char type) {
		this.src1 = src1;
		this.src2 = src2;
		this.dest = dest;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return dest.toString() + " = " + src1.toString() + " " + type + " " + src2.toString() + ";";
	}
}
