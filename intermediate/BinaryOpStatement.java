package intermediate;

import java.util.HashMap;

import helper.CompileException;

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
	public static final int CONCAT = 3; // String + anything.
	
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

	@Override
	public void typeCheck(HashMap<Register, String> regs) throws CompileException {
		// make sure both sides are in the map
		String typeLeft = regs.get(src1);
		if (typeLeft == null) {
			throw new CompileException(src1 + " was used before assigned to.");
		}
		String typeRight = regs.get(src2);
		if (typeRight == null) {
			throw new CompileException(src1 + " was used before assigned to.");
		}
		
		// both sides are in the map, get the resulting type
		// the only op allowed on reference types is +, which should 
		//   be converted to concatenation --- result is String
		if (!src1.isPrimitive() || !src2.isPrimitive()) {
			type = CONCAT;
			dest.type = Register.REFERENCE;
			dest.typeFull = "java/lang/String";
		} else {
			dest.setPrimitiveName(); // should already be set earlier when allocated
		}
		regs.put(dest, dest.typeFull);
	}
}
