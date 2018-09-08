package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

public class CopyStatement implements InterStatement {
	Register src;
	Register dest;
	
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
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(src, regs, fileName, line);
		
		dest.typeFull = src.typeFull;
		regs.put(dest, dest.typeFull);
	}
}
