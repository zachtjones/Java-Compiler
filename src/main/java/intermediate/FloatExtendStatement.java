package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/** Represents a conversion from an integral type (byte, char, int, long) -> floating point (float, double) */
public class FloatExtendStatement implements InterStatement {
	@NotNull private final Register source;
	@NotNull private final Register destination;

	public FloatExtendStatement(@NotNull Register source, @NotNull Register destination) {
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
		return "floatExtend " + destination + " = " + source;
	}
}
