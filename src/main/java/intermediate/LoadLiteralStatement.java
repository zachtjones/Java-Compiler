package intermediate;

import helper.CompileException;
import helper.Types;
import x64.X64File;
import x64.X64Function;
import x64.instructions.LoadEffectiveAddressInstruction;
import x64.instructions.MoveInstruction;
import x64.jni.NewStringUTF_JNI;
import x64.operands.Immediate;
import x64.operands.X64RegisterOperand;

import java.util.HashMap;

import static x64.operands.PCRelativeData.pointerFromLabel;

/** load 10 into %i12; load "hello, world" into %r4, ... */
public class LoadLiteralStatement implements InterStatement, NewStringUTF_JNI {

	public String value;
	// 4 types of literals: char, String, long, double.
	public Register r;
	
	public LoadLiteralStatement(String literalValue, RegisterAllocator regAlloc,
			String fileName, int line) throws CompileException {

		value = literalValue; // or set to something else later.
		if (literalValue.charAt(0) == '"') {
			r = regAlloc.getNext(Types.STRING);
		} else if (literalValue.charAt(0) == '\'') {
			r = regAlloc.getNext(Types.CHAR); // chars are 16-bit
		} else if (literalValue.equals("true")) {
			r = regAlloc.getNext(Types.BOOLEAN);
		} else if (literalValue.equals("false")) {
			r = regAlloc.getNext(Types.BOOLEAN);
		} else if (literalValue.equals("null")) {
			r = regAlloc.getNext(Types.NULL);
		} else if (getLastLetter() == 'f' || getLastLetter() == 'F') {
			r = regAlloc.getNext(Types.FLOAT);
			removeLastLetter();

		} else if (getLastLetter() == 'd' || getLastLetter() == 'D') {
			r = regAlloc.getNext(Types.DOUBLE);
			removeLastLetter();

		} else if (literalValue.contains(".")) {
			// default floating point number is double
			r = regAlloc.getNext(Types.DOUBLE);
		} else if (getLastLetter() == 'l' || getLastLetter() == 'L') {
			r = regAlloc.getNext(Types.LONG);
			removeLastLetter();

		} else {
			// should be an int (bytes & shorts don't have literals)
			r = regAlloc.getNext(Types.INT);
			try {
				Integer.parseInt(literalValue);
			} catch(NumberFormatException e) {
				throw new CompileException("The literal: " + literalValue + " is not a valid literal.",
						fileName, line);
			}
		}
	}

	/** Gets the last letter of the value field */
	private char getLastLetter() {
		return value.charAt(value.length() - 1);
	}

	/** Removes the last letter from value */
	private void removeLastLetter() {
		value = value.substring(0, value.length() - 1);
	}

	
	@Override
	public String toString() {
		return "load " + value + " to " + r.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
			HashMap<String, Types> params, InterFunction func) throws CompileException {
		// only literal class is already set in constructor
		regs.put(r, r.getType());
	}

	@Override
	public void compile(X64File assemblyFile, X64Function function) throws CompileException {
		if (r.getType() == Types.BOOLEAN) {
			if (value.equals("true")) {
				function.addInstruction(
					new MoveInstruction(
						new Immediate(1),
						r.toX64()
					)
				);
			} else {
				function.addInstruction(
					new MoveInstruction(
						new Immediate(0),
						r.toX64()
					)
				);
			}
		} else if (r.getType() == Types.BYTE) {
			function.addInstruction(
				new MoveInstruction(
					new Immediate(Byte.parseByte(value)),
					r.toX64()
				)
			);
		} else if (r.getType() == Types.CHAR) {
			function.addInstruction(
				new MoveInstruction(
					// there is a char literal allows by the GNU as, but will just use the byte number
					new Immediate(value.charAt(0)),
					r.toX64()
				)
			);
		} else if (r.getType() == Types.SHORT) {
			function.addInstruction(
				new MoveInstruction(
					new Immediate(Short.parseShort(value)),
					r.toX64()
				)
			);
		} else if (r.getType() == Types.INT) {
			function.addInstruction(
				new MoveInstruction(
					new Immediate(Integer.parseInt(value)),
					r.toX64()
				)
			);
		} else if (r.getType() == Types.LONG) {
			function.addInstruction(
				new MoveInstruction(
					new Immediate(Long.parseLong(value)),
					r.toX64()
				)
			);
		} else if (r.getType().getIntermediateRepresentation().equals(Types.STRING.getIntermediateRepresentation())) {
			// trim off the " and the beginning and the end, insert into data segment
			String label = assemblyFile.insertDataString(value.substring(1, value.length() - 1));

			// leaq LABEL(%rip), %temp
			X64RegisterOperand chars = function.getNextQuadRegister();
			function.addInstruction(
				new LoadEffectiveAddressInstruction(
					pointerFromLabel(label),
					chars
				)
			);

			// NewStringUTF(JNIEnv, %temp) -> result
			addNewStringUTF_JNI(function, chars, r);
		} else {
			// float and double
			throw new CompileException("Floating point literals not implemented yet", "", -1);
		}
	}
}
