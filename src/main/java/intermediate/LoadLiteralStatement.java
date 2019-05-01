package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.pseudo.LoadEffectiveAddressRIPPseudo;
import x64.jni.NewStringUTF_JNI;
import x64.operands.Immediate;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MoveImmToPseudo;

import java.util.HashMap;

/** load 10 into %i12; load "hello, world" into %r4, ... */
public class LoadLiteralStatement implements InterStatement, NewStringUTF_JNI {

	@NotNull public String value;
	// 4 types of literals: char, String, long, double.
	@NotNull public final Register r;

	private long val;

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
		} else if (value.endsWith("f") || value.endsWith("F")) {
			r = regAlloc.getNext(Types.FLOAT);
			removeLastLetter();

		} else if (value.endsWith("d") || value.endsWith("D")) {
			r = regAlloc.getNext(Types.DOUBLE);
			removeLastLetter();

		} else if (literalValue.contains(".")) {
			// default floating point number is double
			r = regAlloc.getNext(Types.DOUBLE);
		} else {
			// integral type: int or long

			// if it ends in l or L, it's a long, otherwise is 'int' size
			boolean isLong = value.endsWith("l") || value.endsWith("L");
			if (isLong) {
				removeLastLetter();
			}

			// remove underscores
			value = value.replace("_", "");

			// detect based on the starting characters - starts with 0x it's hex, 0b it's binary, 0 it's octal
			//   otherwise decimal (base 10)
			if (value.equals("0")) {
				val = 0;
			} else if (value.startsWith("0x") || value.startsWith("0X")) {
				val = Long.parseLong(value.substring(2), 16);
			} else if (value.startsWith("0b") || value.startsWith("0B")) {
				val = Long.parseLong(value.substring(2), 2);
			} else if (value.startsWith("0")) { // Octal
				val = Long.parseLong(value.substring(1), 8);
			} else {
				val = Long.parseLong(value);
			}

			// in order to not have to cast literals, treat this as the smallest type that it fits into
			//   unless explicitly is a long
			if (isLong) {
				r = regAlloc.getNext(Types.LONG);
			} else if (val <= Byte.MAX_VALUE) {
				r = regAlloc.getNext(Types.BYTE);
			} else if (val <= Short.MAX_VALUE) {
				r = regAlloc.getNext(Types.SHORT);
			} else if (val <= Integer.MAX_VALUE) {
				r = regAlloc.getNext(Types.INT);
			} else { // number larger than 32 bits without 'l' or 'L' suffix
				throw new CompileException("Integer literal larger than the maximum size.", fileName, line);
			}
		}
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
		if (r.getType().equals(Types.BOOLEAN)) {
			if (value.equals("true")) {
				context.addInstruction(
					new MoveImmToPseudo(
						new Immediate(1),
						r.toX64()
					)
				);
			} else {
				context.addInstruction(
					new MoveImmToPseudo(
						new Immediate(0),
						r.toX64()
					)
				);
			}
		} else if (r.getType().equals(Types.CHAR)) {
			// value = '2', or 'a', or something like that
			context.addInstruction(
				new MoveImmToPseudo(
					// there is a char literal allows by the GNU as, but will just use the byte number
					new Immediate(value.charAt(1)),
					r.toX64()
				)
			);

		} else if (r.getType().equals(Types.NULL)) {

			// load null, which is 0
			context.addInstruction(
				new MoveImmToPseudo(
					new Immediate(0),
					r.toX64()
				)
			);

		} else if (r.getType().equals(Types.STRING)) {
			// trim off the " and the beginning and the end, insert into data segment
			String label = context.insertDataString(value.substring(1, value.length() - 1));

			// leaq LABEL(%rip), %temp
			X64PseudoRegister chars = context.getNextQuadRegister();
			context.addInstruction(
				new LoadEffectiveAddressRIPPseudo(
					RIPRelativeData.pointerFromLabel(label),
					chars
				)
			);

			// NewStringUTF(JNIEnv, %temp) -> result
			addNewStringUTF_JNI(context, chars, r);

		} else if (r.getType().equals(Types.FLOAT) || r.getType().equals(Types.DOUBLE)) {
			// not implemented yet
			throw new CompileException("Floating point literals not implemented yet", "", -1);
		} else {

			// in x64, can load a 64-bit immediate value into a register
			context.addInstruction(
				new MoveImmToPseudo(
					new Immediate(val),
					r.toX64()
				)
			);

		}
	}
}
