package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.UsageCheck;

/** store %src at %addr */
public class StoreAddressStatement implements InterStatement {
	Register src;
	Register addr;
	
	public StoreAddressStatement(Register src, Register addr) {
		this.src = src;
		this.addr = addr;
	}
	
	@Override
	public String toString() {
		return "store " + src.toString() + " at " + addr.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(addr, regs);
		UsageCheck.verifyDefined(src, regs);
		
		TypeChecker.checkAssign(src, addr);
	}
}
