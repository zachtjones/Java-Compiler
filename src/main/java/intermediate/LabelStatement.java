package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;

public class LabelStatement implements InterStatement {
	String name;
	
	public LabelStatement(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name + ": ;";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		// nothing needed.
	}
}
