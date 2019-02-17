package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.pseudo.LoadEffectiveAddressRIPPseudo;
import x64.instructions.MoveInstruction;
import x64.jni.NewStringUTF_JNI;
import x64.operands.Immediate;
import x64.operands.RIPRelativeData;
import x64.operands.X64PreservedRegister;

import java.util.HashMap;

/** load 10 into %i12; load "hello, world" into %r4, ... */
public class LoadLiteralStatement implements InterStatement, NewStringUTF_JNI {

	@NotNull public String value;
	// 4 types of literals: char, String, long, double.
	@NotNull public final Register r;
	
	public LoadLiteralStatement(@NotNull String literalValue, @NotNull RegisterAllocator regAlloc,
			@NotNull String fileName, int line) throws CompileException {

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
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		// only literal class is already set in constructor
		regs.put(r, r.getType());
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		if (r.getType() == Types.BOOLEAN) {
			if (value.equals("true")) {
				context.addInstruction(
					new MoveInstruction(
						new Immediate(1),
						r.toX64()
					)
				);
			} else {
				context.addInstruction(
					new MoveInstruction(
						new Immediate(0),
						r.toX64()
					)
				);
			}
		} else if (r.getType() == Types.BYTE) {
			context.addInstruction(
				new MoveInstruction(
					new Immediate(Byte.parseByte(value)),
					r.toX64()
				)
			);
		} else if (r.getType() == Types.CHAR) {
			context.addInstruction(
				new MoveInstruction(
					// there is a char literal allows by the GNU as, but will just use the byte number
					new Immediate(value.charAt(0)),
					r.toX64()
				)
			);
		} else if (r.getType() == Types.SHORT) {
			context.addInstruction(
				new MoveInstruction(
					new Immediate(Short.parseShort(value)),
					r.toX64()
				)
			);
		} else if (r.getType() == Types.INT) {
			context.addInstruction(
				new MoveInstruction(
					new Immediate(Integer.parseInt(value)),
					r.toX64()
				)
			);
		} else if (r.getType() == Types.LONG) {
			context.addInstruction(
				new MoveInstruction(
					new Immediate(Long.parseLong(value)),
					r.toX64()
				)
			);
		} else if (r.getType().getIntermediateRepresentation().equals(Types.STRING.getIntermediateRepresentation())) {
			// trim off the " and the beginning and the end, insert into data segment
			String label = context.insertDataString(value.substring(1, value.length() - 1));

			// leaq LABEL(%rip), %temp
			X64PreservedRegister chars = context.getNextQuadRegister();
			context.addInstruction(
				new LoadEffectiveAddressRIPPseudo(
					RIPRelativeData.pointerFromLabel(label),
					chars
				)
			);

			// NewStringUTF(JNIEnv, %temp) -> result
			addNewStringUTF_JNI(context, chars, r);
		} else {
			// float and double
			throw new CompileException("Floating point literals not implemented yet", "", -1);
		}
	}
}
