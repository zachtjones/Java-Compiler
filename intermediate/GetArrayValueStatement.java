package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

public class GetArrayValueStatement implements InterStatement {
	
	Register array, index, result;

	/**
	 * Represents getting a value out of an array.
	 */
	public GetArrayValueStatement(Register array, Register index, Register result) {
		this.array = array;
		this.index = index;
		this.result = result;
	}

	public String toString() {
		return "getArrayValue " + result + " = " + array + " @ " + index + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(array, regs);
		UsageCheck.verifyDefined(index, regs);
		
		// make sure the type of the array ends in []
		if (!array.typeFull.endsWith("[]")) {
			throw new CompileException("can't index type: " + array.typeFull);
		}

		result.typeFull = array.typeFull.substring(0, array.typeFull.length() - 2);

		regs.put(result, result.typeFull);
	}
}
