package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import helper.UsageCheck;

/** store %src at %addr */
public class StoreAddressStatement implements InterStatement {
	private Register src;
	private Register addr;
	
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
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(addr, regs, fileName, line);
		UsageCheck.verifyDefined(src, regs, fileName, line);
		
		TypeChecker.checkAssign(src, addr, fileName, line);
	}
}
