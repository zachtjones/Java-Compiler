package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;

/** return; */
public class ReturnVoidStatement implements InterStatement {
	
	@NotNull private final String fileName;
	private final int line;
	
	public ReturnVoidStatement(@NotNull String fileName, int line) {
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "returnVoid ;";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs,
						  @NotNull HashMap<String, Types> locals, @NotNull HashMap<String, Types> params,
						  @NotNull InterFunction func) throws CompileException {
		
		if (!func.returnType.equals(Types.VOID)) {
			throw new CompileException("can't return void on non-void function", fileName, line);
		}
	}
}
