package intermediate;

import java.util.HashMap;

import helper.CompileException;

/** Represents the starting of a scope of a local variable. */
public class StartScopeStatement implements InterStatement {
	String name;
	String type;
	
	/**
	 * Constructs a local variable scope starting statement.
	 * @param name The name of the local variable.
	 * @param type The type of the local variable (in IL representation)
	 */
	public StartScopeStatement(String name, String type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return "StartScope " + name + " - " + type + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		// checking if it is already defined is not necessary since this is done
		//  at high level code (AST -> Compile)
		locals.put(name, type);
	}
}
