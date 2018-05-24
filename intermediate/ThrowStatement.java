package intermediate;

/** throw REGISTER */
public class ThrowStatement implements InterStatement {
	int registerNum;
	
	public ThrowStatement(int registerNum) {
		this.registerNum = registerNum;
	}
	
	@Override
	public String toString() {
		return "throw %" + registerNum + ";";
	}
}
