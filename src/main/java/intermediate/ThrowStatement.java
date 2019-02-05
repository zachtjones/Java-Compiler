package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;

/** throw REGISTER */
public class ThrowStatement implements InterStatement {
	@NotNull private final Register r;
	
	@NotNull private final String fileName;
	private final int line;
	
	public ThrowStatement(@NotNull Register r, @NotNull String fileName, int line) {
		this.r = r;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "throw " + r.toString() + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		UsageCheck.verifyDefined(r, regs, fileName, line);
	}
}
