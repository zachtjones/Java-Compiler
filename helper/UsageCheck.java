package helper;

import java.util.HashMap;

import intermediate.Register;

public class UsageCheck {

	public static void verifyDefined(Register r, HashMap<Register, String> regs) throws CompileException {
		if (!regs.containsKey(r)) {
			throw new CompileException(r + " is used before assigned to.");
		}
	}

}
