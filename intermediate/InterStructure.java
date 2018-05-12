package intermediate;

import java.util.ArrayList;

public class InterStructure {
	
	private boolean isInstance;
	private ArrayList<String> types;
	private ArrayList<String> names;
	private ArrayList<String> values; // null's can be in here if not set initial value.

	public InterStructure(boolean isInstance) {
		this.isInstance = isInstance;
		this.types = new ArrayList<String>();
		this.names = new ArrayList<String>();
		this.values = new ArrayList<String>();
	}

	public void addMember(String type, String name) {
		addMember(type, name, null);
	}
	
	public void addMember(String type, String name, String value) {
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
	
}