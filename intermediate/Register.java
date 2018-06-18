package intermediate;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import tree.CompileHistory;
import tree.Expression;
import tree.SymbolTable;

/**
 * Represents an abstraction of a hardware Register.
 * This class implements Expression, as in some cases the tree needs to access
 * statements with these directly.
 * @author zach jones
 *
 */
public class Register implements Expression {
	
	public static final int BOOLEAN = 0;
	public static final int BYTE = 1;
	public static final int CHAR = 2;
	public static final int SHORT = 3;
	public static final int INT = 4;
	public static final int LONG = 5;
	public static final int FLOAT = 6;
	public static final int DOUBLE = 7;
	public static final int REFERENCE = 8;
	
	public static final int LABEL = 9;
	
	public int num;
	public int type;
	
	/** The type's fully qualified name, set on type checking. */
	public String typeFull;
	
	public Register(int num, int type) {
		this.num = num;
		this.type = type;
	}
	
	@Override
	public String toString() {
		if (typeFull == null) {
			switch(type) {
			case BOOLEAN: return "%t" + num;
			case BYTE: return "%b" + num;
			case CHAR: return "%c" + num;
			case SHORT: return "%s" + num;
			case INT: return "%i" + num;
			case LONG: return "%l" + num;
			case FLOAT: return "%f" + num;
			case DOUBLE: return "%d" + num;
			case REFERENCE: return "%r" + num;
			}
			return "unknown register type: " + type;
		} else {
			return "%" + num + "<" + typeFull + ">";
		}
	}

	/**
	 * Gets the larger type (the one with the most width)
	 * Ex: float & long = float;
	 * @param first The Register constant type of the first item.
	 * @param second The Register constant type of the second item.
	 * @return The Register constant type of the result.
	 */
	public static int getLarger(int first, int second) {
		if (first == second) return first;
		if (first == DOUBLE || second == DOUBLE) return DOUBLE;
		if (first == FLOAT || second == FLOAT) return FLOAT;
		if (first == LONG || second == LONG) return LONG;
		if (first == INT || second == INT) return INT;
		if (first == SHORT || second == SHORT) return SHORT;
		return BYTE;
	}
	
	/** Helper function if this register holds a primitive value. */
	public boolean isPrimitive() {
		return type != REFERENCE;
	}
	
	/** Fills in the typeFull for primitive types. */
	public void setPrimitiveName() {
		switch(type) {
		case BOOLEAN: 
			typeFull = "boolean";
			break;
		case BYTE: 
			typeFull = "byte";
			break;
		case CHAR:
			typeFull = "char";
			break;
		case SHORT:
			typeFull = "short";
			break;
		case INT:
			typeFull = "int";
			break;
		case LONG:
			typeFull = "long";
			break;
		case DOUBLE:
			typeFull = "double";
			break;
		}
	}

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// make a copy statement so the result can be gotten with r.getLast()
		Register result = r.getNext(type);
		f.statements.add(new CopyStatement(this, result));
	}
	
	@Override
	public int hashCode() {
		// xor of num and typeFull
		return num ^ typeFull.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Register) {
			Register o = (Register) other;
			return o.num == this.num && o.type == this.type;
		} else {
			return false;
		}
	}
}
