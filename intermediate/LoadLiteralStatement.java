package intermediate;

import helper.CompileException;

/** load 10 into %i12; load "hello, world" into %r4, ... */
public class LoadLiteralStatement implements InterStatement {

	public String value;
	// 4 types of literals: char, String, long, double.
	public Register r;
	
	public LoadLiteralStatement(String literalValue, RegisterAllocator regAlloc) throws CompileException {
		value = literalValue; // or set to something else later.
		if (literalValue.charAt(0) == '"') {
			r = regAlloc.getNext(Register.REFERENCE);
		} else if (literalValue.charAt(0) == '\'') {
			r = regAlloc.getNext(Register.CHAR); // chars are 16-bit
		} else if (literalValue.equals("true")) {
			r = regAlloc.getNext(Register.BOOLEAN);
		} else if (literalValue.equals("false")) {
			r = regAlloc.getNext(Register.BOOLEAN);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'f') {
			r = regAlloc.getNext(Register.FLOAT);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'F') {
			r = regAlloc.getNext(Register.FLOAT);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'd') {
			r = regAlloc.getNext(Register.DOUBLE);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'D') {
			r = regAlloc.getNext(Register.DOUBLE);
		} else if (literalValue.contains(".")) {
			// default floating point number is double
			r = regAlloc.getNext(Register.DOUBLE);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'l') {
			r = regAlloc.getNext(Register.LONG);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'L') {
			r = regAlloc.getNext(Register.LONG);
		} else {
			// should be an int (bytes & shorts don't have literals)
			r = regAlloc.getNext(Register.INT);
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
