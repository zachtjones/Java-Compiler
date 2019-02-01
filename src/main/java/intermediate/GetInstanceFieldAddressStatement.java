package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;

public class GetInstanceFieldAddressStatement implements InterStatement {
	@NotNull private Register instance;
	@NotNull private String fieldName;
	
	@NotNull private final Register result;
	
	@NotNull private final String fileName;
	private final int line;
	
	/**
	 * Creates a new get static field statement. 
	 * @param instance The instance's register holding it's value.
	 * @param fieldName The field's name.
	 */
	public GetInstanceFieldAddressStatement(@NotNull Register instance, @NotNull String fieldName,
											@NotNull Register result,
											@NotNull String fileName, int line) {
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
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(instance, regs, fileName, line);
		// the type of the object
		String type = instance.getType().getClassName(fileName, line);
		
		InterFile object = JavaCompiler.parseAndCompile(type, fileName, line);
		Types resultType = object.getInstFieldType(fieldName, fileName, line);

		result.setType(Types.pointerOf(resultType));
		regs.put(result, result.getType()); // address is a pointer type
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// handle the details in the store instruction later on
		context.markRegisterAsInstanceFieldAddress(result, instance, fieldName);
	}
}
