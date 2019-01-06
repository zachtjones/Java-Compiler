package intermediate;

import java.util.HashMap;

import helper.CompileException;
import x64.X64File;
import x64.X64Function;
import x64.instructions.MoveInstruction;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import static x64.operands.X64RegisterOperand.of;

/** getParam %register = name */
public class GetParamStatement implements InterStatement {
	Register r;
	private String localName;
	
	private final String fileName;
	private final int line;

	private InterFunction func;
	
	/**
	 * Creates a new get parameter variable statement.
	 * @param r The register number to set
	 * @param localName The parameter to get.
	 */
	public GetParamStatement(Register r, String localName, String fileName, int line) {
		this.r = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getParam " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {

		this.func = func;
		
		if (!params.containsKey(localName)) {
			throw new CompileException("'" + localName + "' not a parameter.", fileName, line);
		}
		
		regs.put(r, params.get(localName));
	}

	@Override
	public void compile(X64File assemblyFile, X64Function function) {

		// the args are as follows: JNI, obj/class, actual args
		if (localName.equals("this")) {
			function.addInstruction(
				new MoveInstruction(
					X64NativeRegister.RSI,
					of(X64PreservedRegister.fromILRegister(r))
				)
			);
		} else {
			// localName is guaranteed to be in the list, as type checking was performed
			int paramIndex = 3 + func.paramNames.indexOf(localName);

			function.addInstruction(
				new MoveInstruction(
					X64NativeRegister.argNumbered(paramIndex),
					of(X64PreservedRegister.fromILRegister(r))
				)
			);
		}
	}
}
