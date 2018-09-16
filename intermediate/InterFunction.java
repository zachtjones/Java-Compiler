package intermediate;

import java.util.ArrayList;
import java.util.HashMap;

import helper.CompileException;

public class InterFunction {
	
	public String name;
	public String returnType;
	public boolean isInstance; // instance or static
	public boolean isInit; // static init or instance init
	
	public ArrayList<String> paramTypes;
	public ArrayList<String> paramNames;
	public boolean lastArgVarargs;
	
	public ArrayList<String> throwsList;
	
	public ArrayList<InterStatement> statements;
	
	
	public InterFunction() {
		this.paramTypes = new ArrayList<String>();
		this.paramNames = new ArrayList<String>();
		this.throwsList = new ArrayList<String>();
		this.statements = new ArrayList<InterStatement>();
	}
	
	@Override
	public String toString() {
		// declaration
		StringBuilder sb = new StringBuilder("function ");
		if (isInstance) {
			sb.append("instance ");
		} else {
			sb.append("static ");
		}
		if (lastArgVarargs) {
			sb.append("varargs ");
		}
		if (isInit) {
			sb.append("init\n");
		} else {
			sb.append(name);
			// parameters
			sb.append(" (");
			for (int i = 0; i < paramTypes.size(); i++) {
				sb.append(paramNames.get(i));
				sb.append('<');
				sb.append(paramTypes.get(i));
				sb.append('>');
				sb.append(' ');
			}
			sb.append(')');

			sb.append(" returns ");
			sb.append(returnType);

			// throws list
			if (this.throwsList.size() != 0) {
				sb.append(" throws(");
				for (String s : throwsList) {
					sb.append(s);
					sb.append(' ');
				}
				sb.append(')');
			}
			sb.append('\n');
		}
		
		// body
		for (InterStatement s : statements) {
			sb.append('\t');
			sb.append(s.toString());
			sb.append('\n');
		}
		
		sb.append("end function\n");
		
		return sb.toString();
	}

	/** Type checks all the statements. 
	 * @throws CompileException If there is an error with type checking.*/
	public void typeCheck() throws CompileException {
		HashMap<Register, String> definitions = new HashMap<Register, String>();
		HashMap<String, String> locals = new HashMap<String, String>();
		// define parameters and fill it in.
		HashMap<String, String> params = new HashMap<String, String>();
		for (int i = 0; i < paramTypes.size(); i++) {
			params.put(paramNames.get(i), paramTypes.get(i));
		}
		for (InterStatement i : statements) {
			i.typeCheck(definitions, locals, params, this);
		}		
	}
}