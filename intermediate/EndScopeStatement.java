package intermediate;

import java.util.HashMap;

import helper.CompileException;

/** Represents the ending of a scope of a local variable. */
public class EndScopeStatement implements InterStatement {
	String name;
	
	/**
	 * Constructs a local variable scope ending statement.
	 * @param name The name of the local variable.
	 */
	public EndScopeStatement(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "EndScope " + name + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		// remove name from the locally defined variables
		locals.remove(name);
	}
}
