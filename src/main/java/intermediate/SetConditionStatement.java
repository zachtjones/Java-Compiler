package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;

public class SetConditionStatement implements InterStatement {
	public static final int GREATEREQUAL = 0;
	public static final int GREATER = 1;
	public static final int LESSEQUAL = 2;
	public static final int LESS = 3;
	public static final int EQUAL = 4; // if they are equal, ==
	public static final int NOTEQUAL = 5;
	
	int type;
	@NotNull private final Register left;
	@NotNull private final Register right;
	@NotNull final Register result;
	
	@NotNull private final String fileName;
	private final int line;
	
	public SetConditionStatement(int type, @NotNull Register left, @NotNull Register right,
								 @NotNull Register result,
								 @NotNull String fileName, int line) {
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
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		if (type != NOTEQUAL && type != EQUAL) { // == and != can be used with objects
			// type is a relational only defined on primitives
			if (!left.isPrimitive() || !right.isPrimitive()) {
				throw new CompileException("Relational operators other than == and"
						+ " != are only defined on primitives.", fileName, line);
			}
		}
		result.setType(Types.BOOLEAN);
		regs.put(result, Types.BOOLEAN);
	}
}
