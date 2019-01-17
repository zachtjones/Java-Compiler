package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;

public class GetArrayValueStatement implements InterStatement {
	
	Register array, index, result;
	
	private final String fileName;
	private final int line;

	/**
	 * Represents getting a value out of an array.
	 */
	public GetArrayValueStatement(Register array, Register index, Register result,
			String fileName, int line) {
		this.array = array;
		this.index = index;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}

	public String toString() {
		return "getArrayValue " + result + " = " + array + " @ " + index + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(array, regs, fileName, line);
		UsageCheck.verifyDefined(index, regs, fileName, line);
		
		// make sure the type of the array ends in []
		if (!array.typeFull.endsWith("[]")) {
			throw new CompileException("can't index type: " + array.typeFull, fileName, line);
		}

		result.typeFull = array.typeFull.substring(0, array.typeFull.length() - 2);

		regs.put(result, result.typeFull);
	}
}
