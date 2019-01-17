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
			r = regAlloc.getNext(Types.UNKNOWN);
		} else if (literalValue.charAt(0) == '\'') {
			r = regAlloc.getNext(Types.CHAR); // chars are 16-bit
		} else if (literalValue.equals("true")) {
			r = regAlloc.getNext(Types.BOOLEAN);
		} else if (literalValue.equals("false")) {
			r = regAlloc.getNext(Types.BOOLEAN);
		} else if (literalValue.equals("null")) {
			r = regAlloc.getNext(Types.UNKNOWN);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'f') {
			r = regAlloc.getNext(Types.FLOAT);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'F') {
			r = regAlloc.getNext(Types.FLOAT);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'd') {
			r = regAlloc.getNext(Types.DOUBLE);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'D') {
			r = regAlloc.getNext(Types.DOUBLE);
		} else if (literalValue.contains(".")) {
			// default floating point number is double
			r = regAlloc.getNext(Types.DOUBLE);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'l') {
			r = regAlloc.getNext(Types.LONG);
		} else if (literalValue.charAt(literalValue.length() - 1) == 'L') {
			r = regAlloc.getNext(Types.LONG);
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

	
	@Override
	public String toString() {
		return "load " + value + " to " + r.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
			HashMap<String, Types> params, InterFunction func) throws CompileException {

		if (!r.isPrimitive()) {
			r.typeFull = "java/lang/String";
		} else {
			r.setPrimitiveName();
		}
		regs.put(r, r.typeFull);
	}

	@Override
	public void compile(X64File assemblyFile, X64Function function) throws CompileException {
		switch (r.typeFull) {
		case "boolean":
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
			break;
		case "byte":
			function.addInstruction(
				new MoveInstruction(
					new Immediate(Byte.parseByte(value)),
					r.toX64()
				)
			);
			break;
		case "char":
			function.addInstruction(
				new MoveInstruction(
					// there is a char literal allows by the GNU as, but will just use the byte number
					new Immediate(value.charAt(0)),
					r.toX64()
				)
			);
			break;
		case "short":
			function.addInstruction(
				new MoveInstruction(
					new Immediate(Short.parseShort(value)),
					r.toX64()
				)
			);
			break;
		case "int":
			function.addInstruction(
				new MoveInstruction(
					new Immediate(Integer.parseInt(value)),
					r.toX64()
				)
			);
			break;
		case "long":
			function.addInstruction(
				new MoveInstruction(
					new Immediate(Long.parseLong(value)),
					r.toX64()
				)
			);
			break;
		case "double":
			// TODO maybe a float/double to long bits, then it can be part of the assembly
			throw new CompileException("Floating point literals not implemented yet", "", -1);
		case "null":
			function.addInstruction(
				new MoveInstruction(
					new Immediate(0), // null is literal 0
					r.toX64()
				)
			);
			break;
		case "java/lang/String":
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

			break;
		}
	}
}
