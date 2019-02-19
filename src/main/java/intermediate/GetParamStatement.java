package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.pseudo.MoveRegToPseudo;

/** getParam %register = name */
public class GetParamStatement implements InterStatement {
	@NotNull private final Register r;
	@NotNull private final String localName;
	
	@NotNull private final String fileName;
	private final int line;

	private InterFunction func;
	
	/**
	 * Creates a new get parameter variable statement.
	 * @param r The register number to set
	 * @param localName The parameter to get.
	 */
	public GetParamStatement(@NotNull Register r, @NotNull String localName, @NotNull String fileName, int line) {
		this.r = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getParam " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {

		this.func = func;
		
		if (!params.containsKey(localName)) {
			throw new CompileException("'" + localName + "' not a parameter.", fileName, line);
		}

		r.setType(params.get(localName));
		regs.put(r, r.getType());
	}

	@Override
	public void compile(@NotNull X64Context context) {

		// the args are as follows: JNI, obj/class, actual args
		if (localName.equals("this")) {
			context.addInstruction(
				new MoveRegToPseudo(
					context.argumentRegister(2),
					r.toX64()
				)
			);
		} else {
			// localName is guaranteed to be in the list, as type checking was performed
			// 1st arg: JNI, 2nd arg: object, 3+ the rest
			// if this is static, 2+ can be the rest
			int paramIndex = (func.isInstance ? 3 : 2) + func.paramNames.indexOf(localName);

			context.addInstruction(
				new MoveRegToPseudo(
					context.argumentRegister(paramIndex),
					r.toX64()
				)
			);
		}
	}
}
