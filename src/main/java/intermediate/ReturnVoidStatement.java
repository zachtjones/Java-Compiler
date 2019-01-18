package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;

/** return; */
public class ReturnVoidStatement implements InterStatement {
	
	private final String fileName;
	private final int line;
	
	public ReturnVoidStatement(String fileName, int line) {
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "returnVoid ;";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs,
			HashMap<String, Types> locals, HashMap<String, Types> params,
			InterFunction func) throws CompileException {
		
		if (func.returnType != null) {
			throw new CompileException("can't return void on non-void function", fileName, line);
		}
	}
}
