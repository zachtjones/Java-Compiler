package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import org.jetbrains.annotations.NotNull;

public class GetLocalAddressStatement implements InterStatement {
	Register r;
	private String localName;
	
	private final String fileName;
	private final int line;
	
	/**
	 * Creates a new get local variable address statement.
	 * @param r The register to set containing the address
	 * @param localName The local variable to get.
	 */
	public GetLocalAddressStatement(Register r, String localName, String fileName, int line) {
		this.r = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getLocalAddress " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		if (!locals.containsKey(localName)) {
			throw new CompileException("local variable: " + localName + " is not defined.",
					fileName, line);
		}
		TypeChecker.canDirectlyAssign(locals.get(localName), r.getType(), fileName, line);
		
		// define the register
		r.setType(Types.pointerOf(locals.get(localName)));
		regs.put(r, r.getType());
	}
}
