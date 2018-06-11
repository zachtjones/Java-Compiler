package intermediate;

/**
 * Represents a SSA choose between statement.
 * This statement is used to keep the resulting IL in 
 * Single-Static-Assignment, where a register is only assigned to once in code.
 * @author zach jones
 */
public class ChooseStatement implements InterStatement {
	Register src1, src2, result;
	
	public ChooseStatement(Register src1, Register src2, Register result) {
		this.src1 = src1;
		this.src2 = src2;
		this.result = result;
	}

	@Override
	public String toString() {
		return result + " = choose(" + src1 + ", " + src2 + ");";
	}
}
