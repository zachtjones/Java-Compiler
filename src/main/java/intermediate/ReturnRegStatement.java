package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.instructions.MoveInstruction;

import static x64.allocation.CallingConvention.returnValueRegister;

/** return register; */
public class ReturnRegStatement implements InterStatement {

	@NotNull private final Register r;
	
	@NotNull private final String fileName;
	private final int line;
	
	public ReturnRegStatement(@NotNull Register regNum, @NotNull String fileName, int line) {
		this.r = regNum;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "return " + r + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		if (func.returnType.equals(Types.VOID)) {
			throw new CompileException("Can't return an expression from void function.", fileName, line);
		}
		UsageCheck.verifyDefined(r, regs, fileName, line);
		TypeChecker.canDirectlyAssign(func.returnType, r.getType(), fileName, line);
	}

	@Override
	public void compile(@NotNull X64Context function) throws CompileException {
		function.addInstruction(
			new MoveInstruction(
				r.toX64(),
				returnValueRegister()
			)
		);
	}
}
