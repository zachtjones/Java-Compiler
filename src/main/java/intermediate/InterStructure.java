package intermediate;

import java.util.ArrayList;

import helper.CompileException;
import helper.Types;

public class InterStructure {
	
	private boolean isInstance;
	private ArrayList<Types> types;
	private ArrayList<String> names;
	private ArrayList<String> values; // null's can be in here if not set initial value.

	public InterStructure(boolean isInstance) {
		this.isInstance = isInstance;
		this.types = new ArrayList<>();
		this.names = new ArrayList<>();
		this.values = new ArrayList<>();
	}

	public void addMember(Types type, String name) {
		addMember(type, name, null);
	}
	
	public void addMember(Types type, String name, String value) {
		this.types.add(type);
		this.names.add(name);
		this.values.add(value);
	}

	public boolean hasMembers() {
		return !this.types.isEmpty();
	}
	
	public String toString() {
		if (!hasMembers()) return "";
		StringBuilder result = new StringBuilder();
		if (this.isInstance) {
			result.append("structure instance\n");
		} else {
			result.append("structure static\n");
		}
		
		for (int i = 0; i < types.size(); i++) {
			result.append('\t');
			result.append(types.get(i));
			result.append(' ');
			result.append(names.get(i));
			String value = values.get(i);
			if (value != null) {
				result.append(" = ");
				result.append(value);
			}
			result.append(';');
			result.append('\n');
		}
		
		result.append("end structure\n\n");
		
		return result.toString();
	}

	/** Gets the type for the structure field, throwing a compile exception if problem. */
	public Types getFieldType(String fieldName, String fileName, int line) throws CompileException {
		for (int i = 0; i < names.size(); i++) {
			if (fieldName.equals(names.get(i))) {
				return types.get(i);
			}
		}
		throw new CompileException("Field " + fieldName + " not present in structure.", fileName, line);
	}
	
}