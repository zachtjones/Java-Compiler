package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.instructions.JumpInstruction;

/** jump LABEL; */
public class JumpStatement implements InterStatement {
	@NotNull private final LabelStatement label;
	
	public JumpStatement(@NotNull LabelStatement label) {
		this.label = label;
	}
	
	public String toString() {
		return "jump " + label.name + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		// nothing needed
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		context.addInstruction(new JumpInstruction(label.name));
	}
}
