package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import x64.X64Context;
import x64.instructions.MoveInstruction;

public class CopyStatement implements InterStatement {
	private final Register src;
	private final Register dest;
	
	private final String fileName;
	private final int line;
	
	public CopyStatement(Register src, Register dest, String fileName, int line) {
		this.src = src;
		this.dest = dest;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "copy " + dest.toString() + " = " + src.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(src, regs, fileName, line);

		dest.setType(src.getType());
		regs.put(dest, dest.getType());
	}

	@Override
	public void compile(X64Context context) throws CompileException {
		// simple move from source to destination
		context.addInstruction(
			new MoveInstruction(
				src.toX64(),
				dest.toX64()
			)
		);
	}
}
