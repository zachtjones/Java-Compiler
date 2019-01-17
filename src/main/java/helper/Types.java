package helper;

import x64.Instruction;

public class Types {

	private final String rep;
	private final boolean isPrimitive;

	private Types(String rep, boolean isPrimitive) {
		this.rep = rep;
		this.isPrimitive = isPrimitive;
	}

	public static final Types VOID = new Types("V", true);
	public static final Types BOOLEAN = new Types("Z", true);
	public static final Types BYTE = new Types("B", true);
	public static final Types CHAR = new Types("C", true);
	public static final Types SHORT = new Types("S", true);
	public static final Types INT = new Types("I", true);
	public static final Types LONG = new Types("J", true);
	public static final Types FLOAT = new Types("F", true);
	public static final Types DOUBLE = new Types("D", true);

	/** this represents that the type isn't known yet, will be resolved at type checking */
	public static final Types UNKNOWN = new Types("", false);

	/** this represents a type that is used to hold a label, registers shouldn't have this type. */
	public static final Types LABEL = new Types("", false);

	/** this represents a type that represents a class */
	public static final Types CLASS = new Types("", false);


	/** Creates a types instance from the fully qualified class name */
	public static Types fromFullyQualifiedClass(String className) {
		return new Types("L" + className + ";", false);
	}

	/** Creates a type instance that is an array of the type passed in. */
	public static Types arrayOf(Types typeOfArray) {
		return new Types("[" + typeOfArray.rep, false);
	}

	/** returns the JNI representation for this type, which is the same as what is printed in the IL */
	public String getIntermediateRepresentation() {
		return rep;
	}

	/** returns the x64 instruction size that would be used with this types instance. */
	public Instruction.Size x64Type() {
		if (this == BOOLEAN || this == BYTE) {
			return Instruction.Size.BYTE;
		}
		if (this == CHAR || this == SHORT) {
			return Instruction.Size.WORD;
		}
		if (this == INT) {
			return Instruction.Size.LONG;
		}
		if (this == LONG) {
			return Instruction.Size.QUAD;
		}
		if (this == FLOAT) {
			return Instruction.Size.SINGLE;
		}
		if (this == DOUBLE) {
			return Instruction.Size.DOUBLE;
		}
		// pointers to classes
		return Instruction.Size.QUAD;
	}

	/** returns true if this resembles a primitive type */
	public boolean isPrimitive() {
		return isPrimitive;
	}

	public void resolveImports(ClassLookup c) {
		throw new RuntimeException("Types.java do the resolve imports thing");
	}
}
