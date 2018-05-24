package intermediate;

/** return register; */
public class ReturnRegStatement implements InterStatement {
	int registerNum;
	
	public ReturnRegStatement(int regNum) {
		this.registerNum = regNum;
	}
	
	@Override
	public String toString() {
		return "return %" + registerNum + ";";
	}
}
