package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.Boxing;
import x64.X64Context;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

import static x64.allocation.CallingConvention.returnValueRegister;

/** return register; */
public class ReturnRegStatement implements InterStatement {

	@NotNull private final Register r;
	
	@NotNull private final String fileName;
	private final int line;

	private Types returnType;
	
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
		
		returnType = func.returnType;
		if (returnType.equals(Types.VOID)) {
			throw new CompileException("Can't return an expression from void function.", fileName, line);
		}
		UsageCheck.verifyDefined(r, regs, fileName, line);
		TypeChecker.assertCanAssignWithBoxing(func.returnType, r.getType(), fileName, line);
	}

	@Override
	public void compile(@NotNull X64Context function) throws CompileException {
		if (TypeChecker.isDirectlyAssignable(r.getType(), returnType)) {
			function.addInstruction(
				new MovePseudoToReg(
					r.toX64(),
					returnValueRegister()
				)
			);
		} else if (TypeChecker.isBoxingAssignable(r.getType(), returnType)) {
			// could be made with less instructions, but another copy is fine since it should get optimized out
			X64PseudoRegister temp = function.getNextRegister(returnType);
			// convert to temp reg
			Boxing.insertNecessaryCode(r.toX64(), temp);

			// move temp to the return register
			function.addInstruction(
				new MovePseudoToReg(
					temp,
					returnValueRegister())
			);
		}
	}
}
