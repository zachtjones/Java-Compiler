package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;

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
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		// remove name from the locally defined variables
		locals.remove(name);
	}
}
