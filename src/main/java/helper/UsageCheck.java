package helper;

import java.util.HashMap;

import intermediate.Register;

public class UsageCheck {

	public static void verifyDefined(Register r, HashMap<Register, Types> regs, String fileName, int line)
			throws CompileException {
		
		if (!regs.containsKey(r)) {
			throw new CompileException(r + " is used before assigned to.", fileName, line);
		}
	}

}
