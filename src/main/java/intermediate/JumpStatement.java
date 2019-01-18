package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;

/** jump LABEL; */
public class JumpStatement implements InterStatement {
	private final LabelStatement label;
	
	public JumpStatement(LabelStatement label) {
		this.label = label;
	}
	
	public String toString() {
		return "jump " + label.name + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		// nothing needed
	}
}
