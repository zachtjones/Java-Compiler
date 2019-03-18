package x64;

import helper.CompileException;
import org.jetbrains.annotations.NotNull;
import x64.operands.X64PseudoRegister;

public class Boxing {

	/** Adds the code to convert the source to destination using either a boxing or unboxing operation. */
	public static void insertNecessaryCode(@NotNull X64PseudoRegister source, X64PseudoRegister destination)
		throws CompileException {

		// TODO implement the conversion
		throw new CompileException("Boxing not implemented yet", "", -1);
	}
}
