package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.Boxing;
import x64.X64Context;
import x64.operands.X64PseudoRegister;
import x64.pseudo.ConvertPseudoInstruction;
import x64.pseudo.MovePseudoToPseudo;

/** PutLocal name = %register */
public class PutLocalStatement implements InterStatement {
	@NotNull private final Register r;
	@NotNull private String localName;
	
	@NotNull private final String fileName;
	private final int line;

	private Types localType;
	
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
		localType = locals.get(localName);
		TypeChecker.assertCanAssign(locals.get(localName), r.getType(), fileName, line);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		// TODO everywhere canDirectlyAssign is invoked, can do an extra instruction that converts between types.

		final X64PseudoRegister destination = context.getLocalVariable(localName);

		final Types sourceType = r.getType();

		if (TypeChecker.isDirectlyAssignable(sourceType, localType)) {
			// simple copy the result over to the destination
			context.addInstruction(
				new MovePseudoToPseudo(r.toX64(), destination)
			);
		} else if (TypeChecker.isCastAssignable(sourceType, localType)) {
			// convert source to middle
			// move middle to destination
			X64PseudoRegister temp = context.getNextRegister(localType);
			context.addInstruction(
				ConvertPseudoInstruction.from(r.toX64(), temp)
			);
			context.addInstruction(
				new MovePseudoToPseudo(temp, destination)
			);

		} else if (TypeChecker.isBoxingAssignable(sourceType, localType)) {
			// box/unbox operation
			Boxing.insertNecessaryCode(r.toX64(), destination);
		}


	}
}
