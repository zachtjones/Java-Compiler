package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import helper.UsageCheck;
import x64.X64File;
import x64.X64Function;
import x64.allocation.CallingConvention;
import x64.instructions.MoveInstruction;

import static x64.allocation.CallingConvention.returnValueRegister;

/** return register; */
public class ReturnRegStatement implements InterStatement {

	private final Register r;
	
	private final String fileName;
	private final int line;
	
	public ReturnRegStatement(Register regNum, String fileName, int line) {
		this.r = regNum;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "return " + r + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		if (func.returnType == null) {
			throw new CompileException("Can't return an expression from void function.", fileName, line);
		}
		UsageCheck.verifyDefined(r, regs, fileName, line);
		TypeChecker.canDirectlyAssign(func.returnType, r.getType(), fileName, line);
	}

	@Override
	public void compile(X64File assemblyFile, X64Function function) throws CompileException {
		function.addInstruction(
			new MoveInstruction(
				r.toX64(),
				returnValueRegister()
			)
		);
	}
}
