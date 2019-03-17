package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import helper.BinaryOperation;
import x64.X64Context;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToPseudo;

import static helper.Types.*;

/** dest = src1 OP src2 */
public class BinaryOpStatement implements InterStatement {

	@NotNull private final Register src1;
	@NotNull private final Register src2;
	@NotNull private final Register dest;
	@NotNull private BinaryOperation type;

	@NotNull private final String fileName;
	private final int line;
	
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
			Types larger = src1.getType().getLarger(src2.getType());
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

		X64PseudoRegister temp = context.getNextQuadRegister();

		context.addInstruction(new MovePseudoToPseudo(src1.toX64(), temp));

		context.addInstruction(type.getInstruction(src2.toX64(), temp));

		context.addInstruction(new MovePseudoToPseudo(temp, dest.toX64()));

	}
}
