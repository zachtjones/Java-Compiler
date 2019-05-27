package intermediate;

import conversions.Conversion;
import helper.BinaryOperation;
import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.X64InstructionSize;
import x64.instructions.MoveImmToReg;
import x64.instructions.PopReg;
import x64.instructions.PushReg;
import x64.instructions.SignExtendAX;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;
import x64.pseudo.*;

import java.util.HashMap;
import java.util.List;

import static helper.BinaryOperation.DIVIDE;
import static helper.Types.BOOLEAN;

/** dest = src1 OP src2 */
public class BinaryOpStatement implements InterStatement {

	@NotNull private final Register src1;
	@NotNull private final Register src2;
	@NotNull private final Register dest;
	@NotNull private BinaryOperation type;

	@NotNull private final String fileName;
	private final int line;

	// conversion used in assignment for the first one
	private List<InterStatement> conversionSrc1;
	private List<InterStatement> conversionSrc2;
	private Register src1Converted;
	private Register src2Converted;
	
	/** Creates a binary operation of the type specified statement */
	public BinaryOpStatement(@NotNull Register src1, @NotNull Register src2, @NotNull Register dest,
							 @NotNull BinaryOperation type, @NotNull String fileName, int line) {
		this.src1 = src1;
		this.src2 = src2;
		this.dest = dest;
		this.type = type;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return dest.toString() + " = " + src1.toString() + " "
				+ type + " " + src2.toString() + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs,
						  @NotNull HashMap<String, Types> locals, @NotNull HashMap<String, Types> params,
						  @NotNull InterFunction func) throws CompileException {
		
		// make sure both sides are in the map
		UsageCheck.verifyDefined(src1, regs, fileName, line);
		UsageCheck.verifyDefined(src2, regs, fileName, line);
		
		// both sides are in the map, get the resulting type
		// the only op allowed on reference types is +, which should 
		//   be converted to concatenation --- result is String
		if (!src1.isPrimitive() || !src2.isPrimitive()) {
			type = BinaryOperation.CONCAT;
			dest.setType(Types.STRING);
		} else {
			// larger size is the resulting type
			if (src1.getType().equals(BOOLEAN) || src2.getType().equals(BOOLEAN)) {
				throw new CompileException("Can't perform operator: " + type.getRepresentation() + " on boolean.",
					fileName, line);
			}
			Types larger = src1.getType().getResult(src2.getType());

			// add the conversions necessary
			src1Converted = func.allocator.getNext(larger);
			src2Converted = func.allocator.getNext(larger);
			conversionSrc1 = Conversion.assignmentConversion(src1, src1Converted, fileName, line);
			conversionSrc2 = Conversion.assignmentConversion(src2, src2Converted, fileName, line);

			dest.setType(larger);
		}
		regs.put(dest, dest.getType());
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// dest = src1 op src2

		// in x64, this is: (we can't modify src1 or src2 in case they're used elsewhere)
		//  mov src1, temp
		//  op src2, temp ---- this means temp = temp op src2
		//  mov temp, dest

		X64PseudoRegister temp = context.getNextRegister(src1Converted.getType());

		// convert src1 to src1Converted
		for (InterStatement s : conversionSrc1) {
			s.compile(context);
		}

		// mov src1Converted -> temp
		context.addInstruction(new MovePseudoToPseudo(src1Converted.toX64(), temp));

		// convert src2 to src2Converted
		for (InterStatement s : conversionSrc2) {
			s.compile(context);
		}

		// do the math operation
		switch (type) {
			case ADD:
				context.addInstruction(new AddPseudoToPseudo(src2Converted.toX64(), temp));
				break;

			case SUBTRACT:
				context.addInstruction(new SubtractPseudoToPseudo(src2Converted.toX64(), temp));
				break;

			case TIMES:
				context.addInstruction(new SignedMultiplyPseudoToPseudo(src2Converted.toX64(), temp));
				break;

			case DIVIDE:
			case MOD:

				// div uses the AX and DX registers as operands and the results (quotient and remainder)
				//  so we need to save/restore those registers
				// the operation will either be EDX:EAX = EDX:EAX /% src2Converted,
				//   or the same with the

				// save
				context.addInstruction(new PushReg(X64Register.RAX));
				context.addInstruction(new PushReg(X64Register.RDX));

				// move the src1Converted to the AX register
				context.addInstruction(
					new MovePseudoToReg(
						src1Converted.toX64(),
						X64Register.RAX
					)
				);

				// sign extend into DX:AX (32-bit or 64-bit version)
				context.addInstruction(
					new SignExtendAX(src1Converted.toX64().getSuffix())
				);

				// perform the operation with src2Converted
				context.addInstruction(new SignedDivisionPseudo(src2Converted.toX64()));

				// quotient is in the AX register, remainder in DX
				if (type == DIVIDE) {
					context.addInstruction(new MoveRegToPseudo(
						X64Register.RAX,
						temp
					));
				} else {
					context.addInstruction(new MoveRegToPseudo(
						X64Register.RDX,
						temp
					));
				}

				// restore
				context.addInstruction(new PopReg(X64Register.RDX));
				context.addInstruction(new PopReg(X64Register.RAX));

				break;
			case AND:
				break;
			case XOR:
				break;
			case OR:
				break;
			case LEFT_SHIFT:
				break;
			case RIGHT_SHIFT_SIGN:
				break;
			case RIGHT_SHIFT_UNSIGNED:
				break;
			case CONCAT:
				break;
		}


		// store the result, no assignment conversion needed
		context.addInstruction(new MovePseudoToPseudo(temp, dest.toX64()));

	}
}
