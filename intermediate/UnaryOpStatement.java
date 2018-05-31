package intermediate;

/** dest = OP src2 */
public class UnaryOpStatement implements InterStatement {
	public static final char BITNOT = '~';
	public static final char LOGNOT = '!';
	
	Register src1;
	Register dest;
	char type;
	
	/** Creates an unary operation statement */
	public UnaryOpStatement(Register src1, Register dest, char type) {
		this.src1 = src1;
		this.dest = dest;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return dest.toString() + " = " + type + " " + src1.toString() + ";";
	}
}
