package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;

/** branch when register is equal to 0. */
public class BranchStatementFalse implements InterStatement {
	private LabelStatement destination;
	Register r; // uses a byte register
	
	private final String fileName;
	private final int line;
	
	
	/** Creates a branch statement (conditional jump) when registerNum == 0. */
	public BranchStatementFalse(LabelStatement destination, Register r,
								String fileName, int line) {
		this.destination = destination;
		this.r = r;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "branch when " + r.toString() + " is false to " + destination.name + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs,
						  @NotNull HashMap<String, Types> locals, @NotNull HashMap<String, Types> params,
						  @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(r, regs, fileName, line);
		if (r.getType() != Types.BOOLEAN) {
			throw new CompileException("cannot convert from: " + r.getType() + " to boolean.",
					this.fileName, this.line);
		}
	}
}
