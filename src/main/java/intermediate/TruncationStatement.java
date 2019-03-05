package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.operands.X64PseudoRegister;
import x64.pseudo.ConvertPseudoToPseudo;

import java.util.HashMap;

/** Represents a truncation of data size to a smaller one.
 * (specifically less ranged one, for example float -> long is truncation)
 */
public class TruncationStatement implements InterStatement {
	@NotNull private final Register source;
	@NotNull private final Register destination;

	public TruncationStatement(@NotNull Register source, @NotNull Register destination) {
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {

		// source will be defined, this instruction is only introduced with a cast
		//  destination should also already have been defined.
		//  both source and destination are primitive types
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// this could be a conversion between integral types, or from floating point to smaller ones
		// I'm creating this other pseudo instruction to truncate to make allocation simpler
		//   -- otherwise, the allocation would have to deal with the same register, different sizes
		context.addInstruction(
			new ConvertPseudoToPseudo(
				source.toX64(),
				destination.toX64()
			)
		);
	}

	@Override
	public String toString() {
		return "truncate " + source + " to " + destination;
	}
}
