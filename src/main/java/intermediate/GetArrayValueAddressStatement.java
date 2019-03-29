package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GetArrayValueAddressStatement implements InterStatement {
	
	@NotNull private Register array, index, result;
	
	@NotNull private final String fileName;
	private final int line;

	/**
	 * Represents getting a value's address out of an array.
	 */
	public GetArrayValueAddressStatement(@NotNull Register array, @NotNull Register index, @NotNull Register result,
										 @NotNull String fileName, int line) {
		this.array = array;
		this.index = index;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}

	public String toString() {
		return "getArrayValueAddress " + result + " = " + array + " @ " + index + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(array, regs, fileName, line);
		UsageCheck.verifyDefined(index, regs, fileName, line);
		
		// make sure the type of the array ends in [], then make a pointer of the element type
		Types resulting = Types.pointerOf(array.getType().removeArray(fileName, line));

		result.setType(resulting);

		regs.put(result, resulting);
	}
}
