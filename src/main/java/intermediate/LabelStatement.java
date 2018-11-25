package intermediate;

import java.util.HashMap;

import helper.CompileException;

public class LabelStatement implements InterStatement {
	String name;
	
	public LabelStatement(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name + ": ;";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		// nothing needed.
	}
}
