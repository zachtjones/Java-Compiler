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

	/** Removes an array dimension, returning the new type. If it can't be removed, throws a CompileException */
	public Types removeArray(String fileName, int line) throws CompileException {
		if (rep.charAt(0) == '[') {
			return new Types(rep.substring(1), isPrimitive);
		} else {
			throw new CompileException("Trying to get array element from non-array type: " + rep, fileName, line);
		}
	}

	/** Creates a new type that is a pointer to the argument */
	public static Types pointerOf(Types type) {
		return new Types("*" + type.rep, false);
	}

	/** Dereferences this pointer type, throwing an CompileException if this isn't a pointer */
	public Types dereferencePointer(String fileName, int line) throws CompileException {
		if (rep.charAt(0) == '*') {
			return new Types(rep.substring(1), isPrimitive);
		} else {
			throw new CompileException("Trying to dereference pointer from non-pointer type: " + rep, fileName, line);
		}
	}

	/** Returns the fully qualified class name from this Type, throwing a CompileException if not a class. */
	public String getClassName(String fileName, int line) throws CompileException {
		if (rep.charAt(0) == 'L' && rep.charAt(rep.length() - 1) == ';') {
			return rep.substring(1, rep.length() - 1);
		}
		throw new CompileException("Trying to get a class out of non-class type", fileName, line);
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
