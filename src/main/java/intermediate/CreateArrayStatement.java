package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;

public class CreateArrayStatement implements InterStatement {

	private Register size;
	Types type;
	Register result;

	/**
	 * A statement that creates an array of the type specified.
	 * @param size The number of elements in the array.
	 * @param type The type of the elements the array contains.
	 * @param result The Register that should hold the result of the creation.
	 */
	public CreateArrayStatement(Register size, Types type, Register result) {
		this.size = size;
		this.type = type;
		this.result = result;
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		result.setType(Types.arrayOf(type));
		regs.put(result, result.getType());
	}

}
