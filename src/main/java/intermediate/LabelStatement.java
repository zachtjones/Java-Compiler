package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.directives.LabelInstruction;

public class LabelStatement implements InterStatement {
	@NotNull public final String name;
	
	public LabelStatement(@NotNull String name) {
		this.name = name;
	}
	
	public String toString() {
		return name + ": ;";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		// nothing needed.
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		context.addInstruction(new LabelInstruction(name));
	}
}
