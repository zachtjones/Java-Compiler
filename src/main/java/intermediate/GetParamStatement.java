package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import x64.X64Context;
import x64.instructions.MoveInstruction;

import static x64.allocation.CallingConvention.argumentRegister;


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
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {

		this.func = func;
		
		if (!params.containsKey(localName)) {
			throw new CompileException("'" + localName + "' not a parameter.", fileName, line);
		}

		r.setType(params.get(localName));
		regs.put(r, r.getType());
	}

	@Override
	public void compile(X64Context context) {

		// the args are as follows: JNI, obj/class, actual args
		if (localName.equals("this")) {
			context.addInstruction(
				new MoveInstruction(
					context.argumentRegister(2),
					r.toX64()
				)
			);
		} else {
			// localName is guaranteed to be in the list, as type checking was performed
			// 1st arg: JNI, 2nd arg: object, 3+ the rest
			// if this is static, 2+ can be the rest
			int paramIndex = (func.isInstance ? 3 : 2) + func.paramNames.indexOf(localName);

			context.addInstruction(
				new MoveInstruction(
					context.argumentRegister(paramIndex),
					r.toX64()
				)
			);
		}
	}
}
