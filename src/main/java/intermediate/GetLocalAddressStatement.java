package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;

import java.util.HashMap;

public class GetLocalAddressStatement implements InterStatement {
	@NotNull private final Register destination;
	@NotNull private String localName;
	
	@NotNull private final String fileName;
	private final int line;
	
	/**
	 * Creates a new get local variable address statement.
	 * @param r The register to set containing the address
	 * @param localName The local variable to get.
	 */
	public GetLocalAddressStatement(@NotNull Register r, @NotNull String localName,
									@NotNull String fileName, int line) {
		this.destination = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getLocalAddress " + destination.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		if (!locals.containsKey(localName)) {
			throw new CompileException("local variable: " + localName + " is not defined.",
					fileName, line);
		}
		// all the type checking needed is done with the store address that gets used

		// define the register
		destination.setType(Types.pointerOf(locals.get(localName)));
		regs.put(destination, destination.getType());
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// handle the details in the store instruction later on
		context.markRegisterAsLocalVariableAddress(localName, destination);
	}
}
