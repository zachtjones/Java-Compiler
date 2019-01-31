package helper;

import java.util.HashMap;

import intermediate.Register;
import org.jetbrains.annotations.NotNull;

public class UsageCheck {

	public static void verifyDefined(@NotNull Register r, @NotNull HashMap<Register, Types> regs,
									 @NotNull String fileName, int line) throws CompileException {
		
		if (!regs.containsKey(r)) {
			throw new CompileException(r + " is used before assigned to.", fileName, line);
		}
	}

}
