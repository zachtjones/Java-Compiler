package intermediate;

import java.util.HashMap;

import helper.CompileException;

/** return; */
public class ReturnVoidStatement implements InterStatement {
	public ReturnVoidStatement() {}
	
	@Override
	public String toString() {
		return "return;";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, 
			HashMap<String, String> locals, HashMap<String, String> params,
			InterFunction func) throws CompileException {
		
		if (func.returnType != null) {
			throw new CompileException("can't return void on non-void function");
		}
	}
}
