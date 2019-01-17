package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;

/** dest = OP src2 */
public class UnaryOpStatement implements InterStatement {
	public static final char BITNOT = '~';
	public static final char LOGNOT = '!';
	public static final char NEGATIVE = '-';

	private final Register src1;
	private final Register dest;
	char type;
	
	private final String fileName;
	private final int line;

	/** Creates an unary operation statement */
	public UnaryOpStatement(Register src1, Register dest, char type, String fileName, int line) {
		this.src1 = src1;
		this.dest = dest;
		this.type = type;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		return dest.toString() + " = " + type + " " + src1.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {

		UsageCheck.verifyDefined(src1, regs, fileName, line);

		// check primitives
		if (!src1.isPrimitive()) {
			throw new CompileException("operator: " + type + " is only defined for primitives, but saw: "
					+ src1, fileName, line);
		}
		if (type == '!' && src1.getType() != Types.BOOLEAN) {
			throw new CompileException("operator: ! is only defined for booleans, but saw: "
					+ src1, fileName, line);
		}
		dest.setType(src1.getType());
		regs.put(dest, src1.getType());
	}
}
