package intermediate;

import java.util.HashMap;

import helper.CompileException;
import main.JavaCompiler;

public class GetStaticFieldAddressStatement implements InterStatement {
	String className;
	String fieldName;
	Register result;
	
	private final String fileName;
	private final int line;
	
	/**
	 * Creates a new get static field statement. 
	 * @param className The class's name.
	 * @param fieldName The field's name.
	 * @param register The result register.
	 */
	public GetStaticFieldAddressStatement(String className, String fieldName, Register register,
			String fileName, int line) {
		
		this.className = className;
		this.fieldName = fieldName;
		this.result = register;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getStaticFieldAddress " + fieldName + " of " + className
				+ " to " + result.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		// get the field type for the static field.
		InterFile object = JavaCompiler.parseAndCompile(className, fileName, line);
		String type = object.getStatFieldType(fieldName, fileName, line);

		regs.put(result, type + "*");
	}
}
