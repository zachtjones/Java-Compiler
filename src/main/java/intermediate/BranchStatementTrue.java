package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.ConditionCode;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.instructions.JumpConditionInstruction;
import x64.operands.Immediate;
import x64.pseudo.ComparePseudoAndImmediate;

/** branch when register is not equal to 0. */
public class BranchStatementTrue implements InterStatement {
	@NotNull private final LabelStatement destination;
	@NotNull Register r; // uses a byte register
	
	@NotNull private final String fileName;
	private final int line;
	
	/** Creates a branch statement (conditional jump) when registerNum != 0. */
	public BranchStatementTrue(@NotNull LabelStatement destination, @NotNull Register r,
							   @NotNull String fileName, int line) {
		this.destination = destination;
		this.r = r;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "branch when " + r.toString() + " is true to " + destination.name + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs,
						  @NotNull HashMap<String, Types> locals, @NotNull HashMap<String, Types> params,
						  @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(r, regs, fileName, line);
		if (r.getType() != Types.BOOLEAN) {
			throw new CompileException("cannot convert from: " + r.getType() + " to boolean.",
					fileName, line);
		}
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// compare r to 0, if it's not equal, then take the branch
		context.addInstruction(
			new ComparePseudoAndImmediate(
				r.toX64(),
				new Immediate(0)
			)
		);
		context.addInstruction(
			new JumpConditionInstruction(
				ConditionCode.NOT_EQUAL,
				destination.name
			)
		);
	}
}
