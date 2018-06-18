package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

public class CopyStatement implements InterStatement {
	Register src;
	Register dest;
	
	public CopyStatement(Register src, Register dest) {
		this.src = src;
		this.dest = dest;
	}
	
	@Override
	public String toString() {
		return "copy " + dest.toString() + " = " + src.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs) throws CompileException {
		UsageCheck.verifyDefined(src, regs);
		
		dest.typeFull = src.typeFull;
		regs.put(dest, dest.typeFull);
	}
}
