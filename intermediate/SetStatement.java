package intermediate;

/**
 * Set REGISTER = CONSTANT;
 * @author zach jones
 */
public class SetStatement implements InterStatement {
	int type;
	int registerNum;
	String constantValue;
	
	/** Creates a SetStatement intermediate node, using the fields
	 * @param type The RegisterType Constant field, specifying the type.
	 * @param registerNum the Register's number (1...infinity)
	 * @param constantValue A string that is the constant value (number, string literal, label)
	 */
	public SetStatement(int type, int registerNum, String constantValue) {
		this.type = type;
		this.registerNum = registerNum;
		this.constantValue = constantValue;
	}
	
	
	@Override
	public String toString() {
		return "set " + RegisterType.toString(type) + registerNum
				+ " = " + constantValue + ";";
	}
}
