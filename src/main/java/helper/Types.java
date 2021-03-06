package helper;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;

public class Types implements Comparable<Types> {

	@NotNull private final String rep;
	private final boolean isPrimitive;

	private Types(@NotNull String rep, boolean isPrimitive) {
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

	/** this represents that the type is a literal null value */
	public static final Types NULL = new Types("<null>", false);

	/** this represents that the type isn't known yet, will be resolved at type checking */
	public static final Types UNKNOWN = new Types("<unknown>", false);

	/** this represents a type that is used to hold a label, registers shouldn't have this type. */
	public static final Types LABEL = new Types("<label>", false);

	/** this represents a type that represents a class */
	public static final Types CLASS = new Types("<class>", false);

	public static final Types STATIC_FUNCTION = new Types("<static_func>", false);

	public static final Types INSTANCE_FUNCTION = new Types("<instance_func>", false);

	/** this is shorthand for Types.fromFullyQualifiedClass("java/lang/String") */
	public static final Types STRING = Types.fromFullyQualifiedClass("java/lang/String");


	/** Creates a types instance from the fully qualified class name */
	public static Types fromFullyQualifiedClass(String className) {
		return new Types("L" + className.replace('.', '/') + ";", false);
	}

	/** Creates a type instance that is an array of the type passed in. */
	public static Types arrayOf(Types typeOfArray) {
		return new Types("[" + typeOfArray.rep, typeOfArray.isPrimitive);
	}

	/** Creates a type from the java representation
	 * @param type "void", "short", ... or a class name with the slashes separating the parts. */
	private static Types fromJavaRepresentation(@NotNull String type) {
		switch (type) {
			case "void": return VOID;
			case "boolean": return BOOLEAN;
			case "byte": return BYTE;
			case "char": return CHAR;
			case "short": return SHORT;
			case "int": return INT;
			case "long": return LONG;
			case "float": return FLOAT;
			case "double": return DOUBLE;
		}

		// recursive case for array types, it's an array of the
		if (type.endsWith("[]")) {
			String sub = type.substring(0, type.length() - 2);
			return arrayOf(fromJavaRepresentation(sub));
		}
		return Types.fromFullyQualifiedClass(type);
	}

	/** Creates a types instance from the Class instance, obtained via classLoader reflection. */
	public static Types fromReflection(@NotNull Class<?> type) {
		return fromJavaRepresentation(type.getCanonicalName());
	}

	/**
	 * Gets the larger type (the one with the most range of values) that is a result of the operation.
	 * Ex: float & long = float;
	 * byte + char => int, and similar stuff
	 * @param other The Register constant type of the second item.
	 * @return The Register constant type of the result.
	 */
	public Types getResult(@NotNull Types other) {
		if (this.equals(DOUBLE) || other.equals(DOUBLE)) return DOUBLE;
		if (this.equals(FLOAT) || other.equals(FLOAT)) return FLOAT;
		if (this.equals(LONG) || other.equals(LONG)) return LONG;
		return INT;
	}

	/** Removes an array dimension, returning the new type. If it can't be removed, throws a CompileException */
	public Types removeArray(@NotNull String fileName, int line) throws CompileException {
		if (isArrayType()) {
			return new Types(rep.substring(1), isPrimitive);
		} else {
			throw new CompileException("Trying to get array element from non-array type: " + rep, fileName, line);
		}
	}

	/** helper method for determining if this type is an array */
	public boolean isArrayType() {
		return rep.charAt(0) == '[';
	}

	/** Creates a new type that is a pointer to the argument */
	public static Types pointerOf(@NotNull Types type) {
		return new Types("*" + type.rep, type.isPrimitive);
	}

	/** Dereferences this pointer type, throwing an CompileException if this isn't a pointer */
	public Types dereferencePointer(@NotNull String fileName, int line) throws CompileException {
		if (rep.charAt(0) == '*') {
			return new Types(rep.substring(1), isPrimitive);
		} else {
			throw new CompileException("Trying to dereference pointer from non-pointer type: " + rep, fileName, line);
		}
	}

	/** Returns the fully qualified class name from this Type, throwing a CompileException if not a class. */
	public String getClassName(@NotNull String fileName, int line) throws CompileException {
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
	public X64InstructionSize x64Type() {
		if (this.equals(BOOLEAN) || this.equals(BYTE)) {
			return X64InstructionSize.BYTE;
		}
		if (this.equals(CHAR) || this.equals(SHORT)) {
			return X64InstructionSize.WORD;
		}
		if (this.equals(INT)) {
			return X64InstructionSize.LONG;
		}
		if (this.equals(LONG)) {
			return X64InstructionSize.QUAD;
		}
		if (this.equals(FLOAT)) {
			return X64InstructionSize.SINGLE;
		}
		if (this.equals(DOUBLE)) {
			return X64InstructionSize.DOUBLE;
		}
		// pointers to classes
		return X64InstructionSize.QUAD;
	}

	/** returns true if this resembles a primitive type.
	 * Note that arrays of primitive types are not primitive. */
	public boolean isPrimitive() {
		return isPrimitive && !rep.startsWith("[");
	}

	/** Resolves the imports on this type, replacing partially qualified names with fully qualified ones
	 * @return A Types instance referring to the fully qualified type, as these are immutable */
	public Types resolveImports(@NotNull ClassLookup c, @NotNull String filename, int line) throws CompileException {
		if (isPrimitive) {
			return this;
		}

		// more complicated case: array of class, use recursion to go down levels until a class is reached
		if (isArrayType()) {
			return Types.arrayOf(
				removeArray(filename, line).resolveImports(c, filename, line)
			);
		}

		// basic case: class type
		String className = this.getClassName(filename, line);
		return fromFullyQualifiedClass(c.getFullName(className));
	}

	@Override
	public int hashCode() {
		return getIntermediateRepresentation().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Types &&
			((Types)o).getIntermediateRepresentation().equals(this.getIntermediateRepresentation());
	}

	@Override
	public String toString() {
		return getIntermediateRepresentation();
	}

	/** Returns the size needed to represent this type (or the pointer size if a class) */
	public int getX64Size() {
		if (this.equals(BOOLEAN) || this.equals(BYTE)) {
			return 1;
		}
		if (this.equals(SHORT) || this.equals(CHAR)) {
			return 2;
		}
		if (this.equals(INT) || this.equals(FLOAT)) {
			return 4;
		}
		// float, double, or pointers are 8 bytes
		return 8;
	}

	/** This is used to compare the ordering of specificity for method selection with overloaded methods.
	 * Precondition -- these must be types that Conversions.methodInvocation are convertible.
	 */
	@Override
	public int compareTo(@NotNull Types o) {

		int thisPrimitive = this.primitiveOrdering();
		int otherPrimitive = o.primitiveOrdering();

		if (thisPrimitive != -1 && otherPrimitive != -1) {
			// both are comparable primitives
			if (thisPrimitive < otherPrimitive) {
				// this is more specific, return -1
				return -1;
			} else if (thisPrimitive > otherPrimitive) {
				return 1;
			}
			return 0; // they are the same primitive
		}
		// TODO compare object / primitives
		return 0;
	}

	/** Returns an int signifying the ordering among primitives for specificity. */
	private int primitiveOrdering() {
		// byte < short < int < long < float < double
		//      < char  <  ...

		if (this.equals(BYTE)) return 0;
		if (this.equals(SHORT)) return 1;
		if (this.equals(CHAR)) return 1; // same as char
		if (this.equals(INT)) return 2;
		if (this.equals(LONG)) return 3;
		if (this.equals(FLOAT)) return 4;
		if (this.equals(DOUBLE)) return 5;

		// not a primitive or boolean -- can't be converted from other primitives
		return -1;
	}

	/** Returns the number of bytes required to represent this register. 1, 2, 4, or 8. */
	public int byteSize() {
		if (this.equals(BYTE) || this.equals(BOOLEAN)) return 1;
		if (this.equals(SHORT) || this.equals(CHAR)) return 2;
		if (this.equals(INT) || this.equals(FLOAT)) return 4;

		return 8; // long, double, pointers.
	}
}
