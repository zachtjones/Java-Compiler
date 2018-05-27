package intermediate;

public class AddStatement implements InterStatement {
	Register src1;
	Register src2;
	Register dest;
	
	/** Creates an addition statement */
	public AddStatement(Register src1, Register src2, Register dest) {
		this.src1 = src1;
		this.src2 = src2;
		this.dest = dest;
	}
	
	@Override
	public String toString() {
		return dest.toString() + " = " + src1.toString() + " + " + src2.toString() + ";";
	}
}
