package intermediate;

import java.util.ArrayList;
import java.util.function.Consumer;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InterStructure {
	
	private boolean isInstance;
	@NotNull private final ArrayList<Types> types;
	@NotNull private final ArrayList<String> names;
	@NotNull private final ArrayList<String> values; // null's can be in here if not set initial value.

	public InterStructure(boolean isInstance) {
		this.isInstance = isInstance;
		this.types = new ArrayList<>();
		this.names = new ArrayList<>();
		this.values = new ArrayList<>();
	}

	void addMember(@NotNull Types type, @NotNull String name, @Nullable String value) {
		this.types.add(type);
		this.names.add(name);
		this.values.add(value);
	}

	@Override
	public String toString() {
		if (this.types.isEmpty()) return "";
		StringBuilder result = new StringBuilder();
		result.append(isInstance ? "structure instance\n" : "structure static\n");
		
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
	@NotNull
	Types getFieldType(String fieldName, String parentClass, String fileName, int line) throws CompileException {
		for (int i = 0; i < names.size(); i++) {
			if (fieldName.equals(names.get(i))) {
				return types.get(i);
			}
		}
		throw new CompileException("Field " + fieldName + " not present in structure " + parentClass, fileName, line);
	}

	/** Returns the offset, in bytes for the fieldName within the structure. */
	int getFieldOffset(@NotNull String fieldName) {
		int offset = 8; // all structure instances have a virtual function table from the start.

		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			Types type = types.get(i);

			int size = type.getX64Size();
			// advance to the alignment for the size
			while (offset % size != 0) offset++;

			if (fieldName.equals(name)) {
				return offset;
			}

			// advance by the size
			offset += size;
		}
		return offset;
	}

	/** calls the consumer on each member of this structure */
	public void forEachMember(@NotNull Consumer<String> consumer) {
		for (String name : names) {
			consumer.accept(name);
		}
	}

	/** returns the field size of the member of this structure */
	public int getFieldSize(@NotNull String member) {
		if (names.indexOf(member) != -1) {
			return types.get(names.indexOf(member)).getX64Size();
		}
		return -1;
	}

	/** returns the total size needed to store instances of this structure */
	int getTotalSize() {
		int offset = 8; // all structure instances have a virtual function table from the start.

		for (Types type : types) {
			int size = type.getX64Size();
			// advance to the alignment for the size
			while (offset % size != 0) offset++;

			// advance by the size
			offset += size;
		}
		return offset;
	}
}