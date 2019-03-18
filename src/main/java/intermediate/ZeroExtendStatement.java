package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.pseudo.ZeroExtendPseudoToPseudo;

import java.util.HashMap;

/** Represents a zero extension from source to destination */
public class ZeroExtendStatement implements InterStatement {
	@NotNull private final Register source;
	@NotNull private final Register destination;

	public ZeroExtendStatement(@NotNull Register source, @NotNull Register destination) {
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {

		// destination type should already be filled in, and source and destination will be primitives.
	}

	@Override
	public String toString() {
		return "zeroExtend " + destination + " = " + source;
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// between integral types only
		context.addInstruction(
			new ZeroExtendPseudoToPseudo(
				source.toX64(),
				destination.toX64()
			)
		);
	}
}
