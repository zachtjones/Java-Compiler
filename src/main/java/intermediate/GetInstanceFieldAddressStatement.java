package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import x64.X64Context;

public class GetInstanceFieldAddressStatement implements InterStatement {
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
	public GetInstanceFieldAddressStatement(Register instance, String fieldName, Register result,
			String fileName, int line) {
		this.instance = instance;
		this.fieldName = fieldName;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getInstanceFieldAddress " + fieldName + " of " + instance.toString() 
			+ " to " + result.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(instance, regs, fileName, line);
		// the type of the object
		String type = instance.getType().getClassName(fileName, line);
		
		InterFile object = JavaCompiler.parseAndCompile(type, fileName, line);
		Types resultType = object.getInstFieldType(fieldName, fileName, line);

		result.setType(Types.pointerOf(resultType));
		regs.put(result, result.getType()); // address is a pointer type
	}

	@Override
	public void compile(X64Context context) throws CompileException {
		// handle the details in the store instruction later on
		context.markRegisterAsInstanceFieldAddress(result, instance, fieldName);
	}
}
