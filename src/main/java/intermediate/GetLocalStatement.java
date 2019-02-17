package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.instructions.MoveInstruction;
import x64.operands.X64PreservedRegister;

/** getLocal %register = name */
public class GetLocalStatement implements InterStatement {
	@NotNull private final Register r;
	@NotNull private final String localName;
	
	@NotNull private final String fileName;
	private final int line;
	
	/**
	 * Creates a new put local variable statement.
	 * @param r The register to set
	 * @param localName The local variable to get.
	 */
	public GetLocalStatement(@NotNull Register r, @NotNull String localName, @NotNull String fileName, int line) {
		this.r = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getLocal " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		if (!locals.containsKey(localName)) {
			throw new CompileException("local variable: " + localName + " is not defined.",
					fileName, line);
		}
		
		// define the register
		r.setType(locals.get(localName));
		regs.put(r, r.getType());
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		final X64PreservedRegister source = context.getLocalVariable(localName);

		// move the register over
		context.addInstruction(
			new MoveInstruction(source, r.toX64())
		);
	}
}
