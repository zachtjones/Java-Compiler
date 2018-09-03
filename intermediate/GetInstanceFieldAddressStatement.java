package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;
import main.JavaCompiler;

public class GetInstanceFieldAddressStatement implements InterStatement {
	Register instance;
	String fieldName;
	
	Register result;
	
	/**
	 * Creates a new get static field statement. 
	 * @param instance The instance's register holding it's value.
	 * @param fieldName The field's name.
	 */
	public GetInstanceFieldAddressStatement(Register instance, String fieldName, Register result) {
		this.instance = instance;
		this.fieldName = fieldName;
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "getInstanceFieldAddress " + fieldName + " of " + instance.toString() 
			+ " to " + result.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(instance, regs);
		// the type of the object
		String type = instance.typeFull;
		
		InterFile object = JavaCompiler.parseAndCompile(type);
		String resultType = object.getInstFieldType(fieldName);
		
		regs.put(result, resultType + "*"); // address is a pointer type
	}
}
