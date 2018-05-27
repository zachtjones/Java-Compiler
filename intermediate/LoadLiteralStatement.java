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
	public Register r;
	
	public LoadLiteralStatement(String literalValue, int registerNum) throws CompileException {
		value = literalValue; // or set to something else later.
		if (literalValue.charAt(0) == '"') {
			type = STRING;
			r = new Register(registerNum, Register.REFERENCE);
		} else if (literalValue.charAt(0) == '\'') {
			type = CHAR;
			r = new Register(registerNum, Register.SHORT); // character are 16-bit in Java
		} else if (literalValue.equals("true")) {
			type = BYTE;
			value = "1";
			r = new Register(registerNum, Register.BYTE);
		} else if (literalValue.equals("false")) {
			type = BYTE;
			value = "0";
			r = new Register(registerNum, Register.BYTE);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'f') {
			type = FLOAT;
			r = new Register(registerNum, Register.FLOAT);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'F') {
			type = FLOAT;
			r = new Register(registerNum, Register.FLOAT);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'd') {
			type = DOUBLE;
			r = new Register(registerNum, Register.DOUBLE);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'D') {
			type = DOUBLE;
			r = new Register(registerNum, Register.DOUBLE);
		} else if (literalValue.contains(".")) {
			type = DOUBLE; // default
			r = new Register(registerNum, Register.DOUBLE);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'l') {
			type = LONG;
			r = new Register(registerNum, Register.LONG);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'L') {
			type = LONG;
			r = new Register(registerNum, Register.LONG);
		} else {
			// should be an int (bytes & shorts don't have literals)
			type = INT;
			r = new Register(registerNum, Register.INT);
			try {
				Integer.parseInt(literalValue);
			} catch(NumberFormatException e) {
				throw new CompileException("The literal: " + literalValue + " is not a valid literal.");
			}
		}
	}

	
	@Override
	public String toString() {
		return "load " + value + " to " + r.toString() + ";";
	}
}
