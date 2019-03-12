package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.ConditionCode;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.pseudo.ComparePseudoPseudo;
import x64.pseudo.SetConditionPseudo;

import static helper.ConditionCode.*;

public class SetConditionStatement implements InterStatement {
	
	@NotNull private final ConditionCode type;
	@NotNull private final Register left;
	@NotNull private final Register right;
	@NotNull final Register result;
	
	@NotNull private final String fileName;
	private final int line;
	
	public SetConditionStatement(@NotNull ConditionCode type, @NotNull Register left, @NotNull Register right,
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
		return "setCondition " + result + " = " + left + " " + type + " " + right + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		if (type != NOT_EQUAL && type != EQUAL) { // == and != can be used with objects
			// type is a relational only defined on primitives
			if (!left.isPrimitive() || !right.isPrimitive()) {
				throw new CompileException("Relational operators other than == and"
						+ " != are only defined on primitives.", fileName, line);
			}
		}
		result.setType(Types.BOOLEAN);
		regs.put(result, Types.BOOLEAN);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		// compare the operands
		// SETcc, where cc is the condition code. Note this sets a byte register/mem
		//  the result byte will have the value 1 if the condition holds, 0 otherwise.

		context.addInstruction(
			new ComparePseudoPseudo(left.toX64(), right.toX64())
		);

		context.addInstruction(
			new SetConditionPseudo(type, context.getNextByteRegister())
		);
	}
}
