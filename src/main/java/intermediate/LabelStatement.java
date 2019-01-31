package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;

public class LabelStatement implements InterStatement {
	String name;
	
	public LabelStatement(String name) {
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
}
