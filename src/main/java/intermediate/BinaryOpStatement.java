package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

/** dest = src1 OP src2 */
public class BinaryOpStatement implements InterStatement {

	public static final int LSHIFT = 0;
	public static final int RSHIFTARITH = 1; // sign-filled
	public static final int RSHIFTLOG = 2; // 0 filled.

	private Register src1;
	private Register src2;
	private Register dest;
	String type;

	private final String fileName;
	private final int line;
	
	/** Creates a binary operation of the type specified statement */
	public BinaryOpStatement(Register src1, Register src2, Register dest, String type,
			String fileName, int line) {
		this.src1 = src1;
		this.src2 = src2;
		this.dest = dest;
		this.type = type;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return dest.toString() + " = " + src1.toString() + " "
				+ type + " " + src2.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, 
			HashMap<String, String> locals, HashMap<String, String> params, 
			InterFunction func) throws CompileException {
		
		// make sure both sides are in the map
		UsageCheck.verifyDefined(src1, regs, fileName, line);
		UsageCheck.verifyDefined(src2, regs, fileName, line);
		
		// both sides are in the map, get the resulting type
		// the only op allowed on reference types is +, which should 
		//   be converted to concatenation --- result is String
		if (!src1.isPrimitive() || !src2.isPrimitive()) {
			type = "CONCAT";
			dest.type = Register.REFERENCE;
			dest.typeFull = "java/lang/String";
		} else {
			dest.setPrimitiveName(); // should already be set earlier when allocated
		}
		regs.put(dest, dest.typeFull);
	}
}
