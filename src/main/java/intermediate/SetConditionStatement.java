package intermediate;

import java.util.HashMap;

import helper.CompileException;

public class SetConditionStatement implements InterStatement {
	public static final int GREATEREQUAL = 0;
	public static final int GREATER = 1;
	public static final int LESSEQUAL = 2;
	public static final int LESS = 3;
	public static final int EQUAL = 4; // if they are equal, ==
	public static final int NOTEQUAL = 5;
	
	int type;
	Register left;
	Register right;
	Register result;
	
	private final String fileName;
	private final int line;
	
	public SetConditionStatement(int type, Register left, Register right, Register result,
			String fileName, int line) {
		this.type = type;
		this.left = left;
		this.right = right;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		String leftPart = "setCondition " + result.toString() + " = " + left.toString();
		if (type == GREATEREQUAL) {
			return leftPart + " >= " + right.toString() + ";";
		} else if (type == GREATER) {
			return leftPart + " > " + right.toString() + ";";
		} else if (type == LESSEQUAL) {
			return leftPart + " <= " + right.toString() + ";";
		} else if (type == LESS){
			return leftPart + " < " + right.toString() + ";";
		} else if (type == EQUAL){
			return leftPart + " == " + right.toString() + ";";
		} else {
			return leftPart + " != " + right.toString() + ";";
		}
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		if (type != NOTEQUAL && type != EQUAL) { // == and != can be used with objects
			// type is a relational only defined on primitives
			if (!left.isPrimitive() || !right.isPrimitive()) {
				throw new CompileException("Relational operators other than == and"
						+ " != are only defined on primitves.", fileName, line);
			}
		}
		result.typeFull = "boolean";
		regs.put(result, result.typeFull);
	}
}
