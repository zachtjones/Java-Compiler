package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.operands.X64RegisterOperand;

/** Represents the starting of a scope of a local variable. */
public class StartScopeStatement implements InterStatement {
	private final String name;
	private final Types type;
	
	/**
	 * Constructs a local variable scope starting statement.
	 * @param name The name of the local variable.
	 * @param type The type of the local variable (in IL representation)
	 */
	public StartScopeStatement(String name, Types type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return "StartScope " + name + " - " + type + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		// checking if it is already defined is not necessary since this is done
		//  at high level code (AST -> Compile)
		locals.put(name, type);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// TODO other sized registers
		X64RegisterOperand allocated = context.getNextQuadRegister();
		context.markRegisterAsLocalVariable(name, allocated);
	}
}
