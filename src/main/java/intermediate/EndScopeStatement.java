package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToPseudo;

import java.util.HashMap;

/** Represents the ending of a scope of a local variable. */
public class EndScopeStatement implements InterStatement {
	@NotNull private final String name;
	
	/**
	 * Constructs a local variable scope ending statement.
	 * @param name The name of the local variable.
	 */
	public EndScopeStatement(@NotNull String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "EndScope " + name + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		// remove name from the locally defined variables
		locals.remove(name);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// do a simple copy so that the register is still possibly around later when needed
		// TODO this will break when optimizations are started to be used, as it's a dead statement.
		// getLocalVariable returns null when it's not a local, parameters also go out of scope
		X64PseudoRegister source = context.getLocalVariable(name);
		if (source != null)
			context.addInstruction(new MovePseudoToPseudo(source, context.getNextRegister(source.getSuffix())));

		context.clearLocalVariable(name);
	}
}
