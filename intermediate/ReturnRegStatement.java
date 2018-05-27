package intermediate;

/** return register; */
public class ReturnRegStatement implements InterStatement {
	Register r;
	
	public ReturnRegStatement(Register regNum) {
		this.r = regNum;
	}
	
	@Override
	public String toString() {
		return "return " + r + ";";
	}
}
