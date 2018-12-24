package intermediate;

import java.util.HashMap;

import helper.CompileException;
import main.JavaCompiler;
import x64.*;
import x64.instructions.MoveInstruction;

import static x64.PCRelativeData.fromField;
import static x64.X64Register.fromILRegister;

public class GetStaticFieldStatement implements InterStatement {
	private final String className;
	private final String fieldName;
	Register result;
	
	private final String fileName;
	private final int line;
	
	/**
	 * Creates a new get static field statement. 
	 * @param className The class's name.
	 * @param fieldName The field's name.
	 * @param register The result register.
	 */
	public GetStaticFieldStatement(String className, String fieldName, Register register,
			String fileName, int line) {
		
		this.className = className;
		this.fieldName = fieldName;
		this.result = register;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getStaticField " + fieldName + " of " + className
				+ " to " + result.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		// get the field type for the static field.
		InterFile object = JavaCompiler.parseAndCompile(className, fileName, line);
		String type = object.getStatFieldType(fieldName, fileName, line);
		
		result.typeFull = type;
		regs.put(result, type);
	}

	@Override
	public void compile(X64Context context, X64File assemblyFile) throws CompileException {
		if (className.startsWith("java/")) {

		} else {
			// mov CLASS_NAME_FIELD_NAME(%rip), %destination
			assemblyFile.instructions.add(
				new MoveInstruction(
					fromField(className, fieldName, result),
					fromILRegister(result)
				)
			);
		}

	}
}
