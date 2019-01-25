package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import main.JavaCompiler;
import x64.X64Context;

public class GetStaticFieldAddressStatement implements InterStatement {
	private String className;
	private String fieldName;
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
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		// get the field type for the static field.
		InterFile object = JavaCompiler.parseAndCompile(className, fileName, line);
		Types type = object.getStatFieldType(fieldName, fileName, line);

		result.setType(Types.pointerOf(type));
		regs.put(result, result.getType());
	}

	@Override
	public void compile(X64Context context) throws CompileException {
		// handle the details in the store instruction later on
		context.markRegisterAsStaticFieldAddress(result, className, fieldName);
	}
}
