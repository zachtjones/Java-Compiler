package intermediate;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import tree.Expression;
import tree.NodeImpl;
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
public class Register extends NodeImpl implements Expression {
	
	public static final int BOOLEAN = 0;
	public static final int BYTE = 1;

	int num;
	@NotNull private Types type;

	public Register(int num, @NotNull Types type, @NotNull String fileName, int line) {
		super(fileName, line);
		this.num = num;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "%" + type.getIntermediateRepresentation() + num;
	}

	/** gets the type of this register */
	@NotNull
	public Types getType() {
		return type;
	}

	/** sets the type of this register */
	public void setType(@NotNull Types type) {
		this.type = type;
	}

	/**
	 * Gets the larger type (the one with the most width)
	 * Ex: float & long = float;
	 * @param first The Register constant type of the first item.
	 * @param second The Register constant type of the second item.
	 * @return The Register constant type of the result.
	 */
	public static Types getLarger(Types first, Types second) {
		if (first == second) return first;
		if (first == Types.DOUBLE || second == Types.DOUBLE) return Types.DOUBLE;
		if (first == Types.FLOAT || second == Types.FLOAT) return Types.FLOAT;
		if (first == Types.LONG || second == Types.LONG) return Types.LONG;
		if (first == Types.INT || second == Types.INT) return Types.INT;
		if (first == Types.SHORT || second == Types.SHORT) return Types.SHORT;
		return Types.BYTE;
	}
	
	/** Helper function if this register holds a primitive value. */
	boolean isPrimitive() {
		return type.isPrimitive();
	}

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// make a copy statement so the result can be gotten with r.getLast()
		Register result = f.allocator.getNext(type);
		f.statements.add(new CopyStatement(this, result, getFileName(), getLine()));
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

	/** Returns the instruction size that is suitable for this instruction, for the x64 architecture */
	public Instruction.Size x64Type() {
		return type.x64Type();
    }

    /** Converts this intermediate language register to the x64 assembly type. */
    public X64RegisterOperand toX64() {
		return of(new X64PreservedRegister(this.num, this.x64Type()));
	}
}
