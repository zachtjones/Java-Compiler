package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToPseudo;
import x64.pseudo.NegationPseudo;
import x64.pseudo.NotPseudo;
import x64.pseudo.XorImmToPseudo;

/** dest = OP src1 */
public class UnaryOpStatement implements InterStatement {
	public static final char BITNOT = '~';
	public static final char LOGNOT = '!';
	public static final char NEGATIVE = '-';

	@NotNull private final Register src1;
	@NotNull private final Register dest;
	char type;
	
	@NotNull private final String fileName;
	private final int line;

	/** Creates an unary operation statement */
	public UnaryOpStatement(@NotNull Register src1, @NotNull Register dest, char type,
							@NotNull String fileName, int line) {
		this.src1 = src1;
		this.dest = dest;
		this.type = type;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		return dest.toString() + " = " + type + " " + src1.toString() + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {

		UsageCheck.verifyDefined(src1, regs, fileName, line);

		// check primitives
		if (!src1.isPrimitive()) {
			throw new CompileException("operator: " + type + " is only defined for primitives, but saw: "
					+ src1, fileName, line);
		}
		if (type == LOGNOT && src1.getType() != Types.BOOLEAN) {
			throw new CompileException("operator: ! is only defined for booleans, but saw: "
					+ src1, fileName, line);
		}
		dest.setType(src1.getType());
		regs.put(dest, src1.getType());
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		// copy src1 to the destination, then modify the destination

		context.addInstruction(new MovePseudoToPseudo(
			src1.toX64(),
			dest.toX64()
		));

		// perform the operation

		switch (type) {
			case BITNOT:
				// bitwise 1's complement, instruction not

				context.addInstruction(new NotPseudo(
					dest.toX64()
				));
				break;


			case LOGNOT:
				// we want 1 -> 0, 0 -> 1
				// xor $1, dest

				context.addInstruction(
					new XorImmToPseudo(
						new Immediate(1),
						dest.toX64()
					)
				);
				break;

			default: // arithmetic negation

				context.addInstruction(new NegationPseudo(
					dest.toX64()
				));
				break;
		}
	}
}
