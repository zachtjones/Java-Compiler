package intermediate;

import conversions.Conversion;
import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.pseudo.MovePseudoToReg;

import java.util.HashMap;
import java.util.List;

import static x64.allocation.CallingConvention.returnValueRegister;

/** return register; */
public class ReturnRegStatement implements InterStatement {

	@NotNull private final Register r;
	
	@NotNull private final String fileName;
	private final int line;

	// these are set in the type check, used in the compile stage
	private Register pseudoReturn;
	private List<InterStatement> conversions;

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

		// these are set in type checking, used in the compile to pseudo asm stage
		Types returnType = func.returnType;
		if (returnType.equals(Types.VOID)) {
			throw new CompileException("Can't return an expression from void function.", fileName, line);
		}
		UsageCheck.verifyDefined(r, regs, fileName, line);

		// create temporary register to act as copy
		pseudoReturn = func.allocator.getNext(returnType);
		conversions = Conversion.assignmentConversion(r, pseudoReturn, fileName, line);
	}

	@Override
	public void compile(@NotNull X64Context function) throws CompileException {
		// compile in the conversion logic, might just be a copy
		for (InterStatement i : conversions)
			i.compile(function);

		// move temp to the return register
		function.addInstruction(
			new MovePseudoToReg(
				pseudoReturn.toX64(),
				returnValueRegister())
		);
	}
}
