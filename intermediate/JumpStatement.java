package intermediate;

import java.util.HashMap;

import helper.CompileException;

/** jump LABEL; */
public class JumpStatement implements InterStatement {
	LabelStatement label;
	
	public JumpStatement(LabelStatement label) {
		this.label = label;
	}
	
	public String toString() {
		return "jump " + label.name + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		// nothing needed
	}
}
