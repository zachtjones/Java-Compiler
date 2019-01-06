package intermediate;

import helper.ClassLookup;
import helper.CompileException;
import tree.Expression;
import tree.SymbolTable;
import x64.Instruction;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import static x64.operands.X64RegisterOperand.of;

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
	public static final int NULL = 9;
	public static final int LABEL = 10;
	public static final int VOID = 11;
	
	public int num;
	public int type;
	
	/** The type's fully qualified name, set on type checking. */
	public String typeFull;
	
	private final String fileName;
	private final int line;
	
	public Register(int num, int type, String fileName, int line) {
		this.num = num;
		this.type = type;
		this.fileName = fileName;
		this.line = line;
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
			case VOID: return "%v" + num;
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
		case NULL:
			typeFull = "null";
			break;
		case VOID:
			typeFull = "void";
			break;
		}
	}

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// make a copy statement so the result can be gotten with r.getLast()
		Register result = f.allocator.getNext(type);
		f.statements.add(new CopyStatement(this, result, fileName, line));
	}
	
	@Override
	public int hashCode() {
		// the register number
		return num;
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

	@Override
	public String getFileName() { // required by Expression, but not needed here
		return "";
	}

	@Override
	public int getLine() {
		// return -1, line doesn't exist
		return -1;
	}

	/** Returns the instruction size that is suitable for this instruction, for the x64 architecture */
	public Instruction.Size x64Type() {
		switch(type) {
			case BOOLEAN:
			case BYTE:
				return Instruction.Size.BYTE;

			case CHAR:
			case SHORT:
				return Instruction.Size.WORD;

			case INT:
				return Instruction.Size.LONG;

			case LONG:
				return Instruction.Size.QUAD;

			case FLOAT:
				return Instruction.Size.SINGLE;

			case DOUBLE:
				return Instruction.Size.DOUBLE;
		}
		// references / nulls
		return Instruction.Size.QUAD;
    }

    /** Converts this intermediate language register to the x64 assembly type. */
    public X64RegisterOperand toX64() {
		return of(new X64PreservedRegister(this.num, this.x64Type()));
	}
}
