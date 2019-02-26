package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CastStatement implements InterStatement {

	@NotNull private final Register source;
	@NotNull private final Register destination;
	@NotNull private final Types type;
	@NotNull private final String fileName;
	private final int line;

	public CastStatement(@NotNull Register source, @NotNull Register destination, @NotNull Types type,
						 @NotNull String fileName, int line) {

		this.source = source;
		this.destination = destination;
		this.type = type;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params,
						  @NotNull InterFunction func) throws CompileException {

		UsageCheck.verifyDefined(source, regs, fileName, line);

		destination.setType(type);
		regs.put(destination, type);
	}

	@Override
	public String toString() {
		return "cast " + source + " to " + destination;
	}
}
