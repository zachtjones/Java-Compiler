package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CreateArrayStatement implements InterStatement {

	@NotNull private Register size;
	@NotNull private final Types type;
	@NotNull private final Register result;

	/**
	 * A statement that creates an array of the type specified.
	 * @param size The number of elements in the array.
	 * @param type The type of the elements the array contains.
	 * @param result The Register that should hold the result of the creation.
	 */
	public CreateArrayStatement(@NotNull Register size, @NotNull Types type, @NotNull Register result) {
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

	@Override
	public String toString() {
		return "createArray " + result + " = new " + type + "[" + size + "]";
	}
}
