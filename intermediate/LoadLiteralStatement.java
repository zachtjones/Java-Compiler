package intermediate;

import helper.CompileException;

/** load 10 into %i12; load "hello, world" into %r4, ... */
public class LoadLiteralStatement implements InterStatement {
	public final static int CHAR = 0;
	public final static int BYTE = 1; // used for storing booleans
	public final static int STRING = 2;
	public final static int INT = 3;
	public final static int LONG = 4;
	public final static int FLOAT = 5;
	public final static int DOUBLE = 6;
	
	public int type;
	public String value;
	// 4 types of literals: char, String, long, double.
	public int registerNum;
	
	public LoadLiteralStatement(String literalValue, int registerNum) throws CompileException {
		if (literalValue.charAt(0) == '"') {
			type = STRING;
		} else if (literalValue.charAt(0) == '\'') {
			type = CHAR;
		} else if (literalValue.equals("true")) {
			type = BYTE;
			value = "1";
		} else if (literalValue.equals("false")) {
			type = BYTE;
			value = "0";
		} else if (literalValue.charAt(literalValue.length() - 1) == 'f') {
			type = FLOAT;
		} else if (literalValue.charAt(literalValue.length() - 1) == 'F') {
			type = FLOAT;
		} else if (literalValue.charAt(literalValue.length() - 1) == 'd') {
			type = DOUBLE;
		} else if (literalValue.charAt(literalValue.length() - 1) == 'D') {
			type = DOUBLE;
		} else if (literalValue.contains(".")) {
			type = DOUBLE; // default
		} else if (literalValue.charAt(literalValue.length() - 1) == 'l') {
			type = LONG;
		} else if (literalValue.charAt(literalValue.length() - 1) == 'L') {
			type = LONG;
		} else {
			// should be an int (bytes & shorts don't have literals)
			type = INT;
			try {
				Integer.parseInt(literalValue);
			} catch(NumberFormatException e) {
				throw new CompileException("The literal: " + literalValue + " is not a valid literal.");
			}
		}
	}

	
	@Override
	public String toString() {
		char reg;
		switch(type) {
		case CHAR:
			reg = 'c';
			break;
		case BYTE:
			reg = 'b';
			break;
		case STRING:
			reg = 'r';
			break;
		case INT:
			reg = 'i';
			break;
		case LONG:
			reg = 'l';
			break;
		case FLOAT:
			reg = 'f';
			break;
		default: // double
			reg = 'd';
			break;
		}
		return "load " + value + " to %" + reg + registerNum + ";";
	}
}
