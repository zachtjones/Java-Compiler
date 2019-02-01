package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;

public class InstanceOfStatement implements InterStatement {
	
	@NotNull private final Register source;
	@NotNull private final String ilClassName;
	@NotNull private final Register result;
	
	@NotNull private final String fileName;
	private final int line;
	

	public InstanceOfStatement(@NotNull Register source, @NotNull String ilClassName, @NotNull Register result,
							   @NotNull String fileName, int line) {
		
		this.source = source;
		this.ilClassName = ilClassName;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(source, regs, fileName, line);
		// verify the class exists
		JavaCompiler.parseAndCompile(ilClassName, fileName, line);
	}
	
	@Override
	public String toString() {
		return "checkInstance " + result + " = " + source + " instanceof " + ilClassName;
	}

}
