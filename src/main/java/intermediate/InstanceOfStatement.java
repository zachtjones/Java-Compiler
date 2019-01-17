package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;

public class InstanceOfStatement implements InterStatement {
	
	private Register source;
	private String ilClassName;
	private Register result;
	
	private final String fileName;
	private final int line;
	

	public InstanceOfStatement(Register source, String ilClassName, Register result, 
			String fileName, int line) {
		
		this.source = source;
		this.ilClassName = ilClassName;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(source, regs, fileName, line);
		// verify the class exists
		JavaCompiler.parseAndCompile(ilClassName, fileName, line);
		
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "checkInstance " + result + " = " + source + " instanceof " + ilClassName;
	}

}
