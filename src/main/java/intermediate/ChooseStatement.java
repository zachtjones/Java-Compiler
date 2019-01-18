package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;

/**
 * Represents a SSA choose between statement.
 * This statement is used to keep the resulting IL in 
 * Single-Static-Assignment, where a register is only assigned to once in code.
 * @author zach jones
 */
public class ChooseStatement implements InterStatement {
	Register src1, src2, result;
	
	private final String fileName;
	private final int line;
	
	public ChooseStatement(Register src1, Register src2, Register result,
			String fileName, int line) {
		
		this.src1 = src1;
		this.src2 = src2;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		return result + " = choose(" + src1 + ", " + src2 + ");";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		// TODO left and right have to have common superclass, and the result
		//   is that common superclass
		// primitive types should already be done
		// remember the boxing / un-boxing operations too
		if (!src1.isPrimitive() || !src2.isPrimitive()) {
			System.out.println("Choose statements type checking not complete for reference types");
		}
		if ( ! src1.getType().getIntermediateRepresentation().equals(src2.getType().getIntermediateRepresentation())) {
			throw new CompileException("in the choose statement, the types were different. "
					+ "Add code to see if they are convertible.", fileName, line);
		}
		result.setType(src1.getType());
		regs.put(result, result.getType());
	}
}
