package intermediate;

import conversions.Conversion;
import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.pseudo.MovePseudoToPseudo;

import java.util.HashMap;
import java.util.List;

/** PutLocal name = %register */
public class PutLocalStatement implements InterStatement {
	@NotNull private final Register r;
	@NotNull private String localName;
	
	@NotNull private final String fileName;
	private final int line;

	// set by the type check phase, used in the compile -> pseudo asm
	private Register intermediateReg;
	private List<InterStatement> conversions;

	/**
	 * Creates a new put local variable statement.
	 * @param r The register to use it's value
	 * @param localName The local variable to set.
	 */
	public PutLocalStatement(@NotNull Register r, @NotNull String localName, @NotNull String fileName, int line) {
		this.r = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "PutLocal " + localName + " = " + r.toString() + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(r, regs, fileName, line);
		if (!locals.containsKey(localName)) {
			throw new CompileException("local variable: " + localName + " is not defined.",
					fileName, line);
		}
		Types localType = locals.get(localName);

		// create temporary register to act as copy
		intermediateReg = func.allocator.getNext(localType);
		conversions = Conversion.assignmentConversion(r, intermediateReg, fileName, line);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		// add in the conversion
		for (InterStatement i : conversions) {
			i.compile(context);
		}

		// move the intermediate result to the final part
		context.addInstruction(
			new MovePseudoToPseudo(
				intermediateReg.toX64(),
				context.getLocalVariable(localName)
			)
		);
	}
}
