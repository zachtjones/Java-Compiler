package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

/** dest = OP src2 */
public class UnaryOpStatement implements InterStatement {
	public static final char BITNOT = '~';
	public static final char LOGNOT = '!';
	public static final char NEGATIVE = '-';

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

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {

		UsageCheck.verifyDefined(src1, regs);

		// check primitives
		if (!src1.isPrimitive()) {
			throw new CompileException("operator: " + type + " is only defined for primitives, but saw: " + src1);
		}
		if (type == '!' && src1.type != Register.BOOLEAN) {
			throw new CompileException("operator: ! is only defined for booleans, but saw: " + src1);
		}
		dest.typeFull = src1.typeFull;
		regs.put(dest, dest.typeFull);
	}
}
