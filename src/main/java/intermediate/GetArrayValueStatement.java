package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;

public class GetArrayValueStatement implements InterStatement {
	
	@NotNull private final Register array, index, result;
	
	@NotNull private final String fileName;
	private final int line;

	/**
	 * Represents getting a value out of an array.
	 */
	public GetArrayValueStatement(@NotNull Register array, @NotNull Register index, @NotNull Register result,
								  @NotNull String fileName, int line) {
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
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(array, regs, fileName, line);
		UsageCheck.verifyDefined(index, regs, fileName, line);
		
		// make sure the type of the array ends in []
		Types resultingType = array.getType().removeArray(fileName, line);
		array.setType(resultingType);

		regs.put(result, resultingType);
	}
}
