package intermediate;

import java.util.ArrayList;

public class InterFunction {
	
	public String name;
	public String returnType;
	public boolean isInstance; // instance or static
	
	public ArrayList<String> paramTypes;
	
	public ArrayList<String> throwsList;
	
	public ArrayList<InterStatement> statements;
	
	
	public InterFunction() {
		this.paramTypes = new ArrayList<String>();
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
		sb.append(name);
		// parameters
		sb.append(" (");
		for (String s : paramTypes) {
			sb.append(s);
			sb.append(' ');
		}
		sb.append(") ");
		
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
		
		// body
		for (InterStatement s : statements) {
			sb.append('\t');
			sb.append(s.toString());
			sb.append('\n');
		}
		
		sb.append("end function\n");
		
		return sb.toString();
	}
}