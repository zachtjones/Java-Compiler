package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.UsageCheck;

/** store %src at %addr */
public class StoreAddressStatement implements InterStatement {
	Register src;
	Register addr;
	
	private final String fileName;
	private final int line;
	
	public StoreAddressStatement(Register src, Register addr, String fileName, int line) {
		this.src = src;
		this.addr = addr;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "store " + src.toString() + " at " + addr.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(addr, regs, fileName, line);
		UsageCheck.verifyDefined(src, regs, fileName, line);
		
		TypeChecker.checkAssign(src, addr, fileName, line);
	}
}
