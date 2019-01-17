package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;

public class GetInstanceFieldStatement implements InterStatement {
	private Register instance;
	private String fieldName;
	
	Register result;
	
	private final String fileName;
	private final int line;
	
	/**
	 * Creates a new get static field statement. 
	 * @param instance The instance's register holding it's value.
	 * @param fieldName The field's name.
	 */
	public GetInstanceFieldStatement(Register instance, String fieldName, Register result,
			String fileName, int line) {
		this.instance = instance;
		this.fieldName = fieldName;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getInstanceField " + fieldName + " of " + instance.toString() 
			+ " to " + result.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {

		UsageCheck.verifyDefined(instance, regs, fileName, line);
		// the type of the object
		Types type = instance.getType();
		
		InterFile object = JavaCompiler.parseAndCompile(type, fileName, line);
		Types resultType = object.getInstFieldType(fieldName, fileName, line);
		
		regs.put(result, resultType);
	}
}
