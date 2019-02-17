package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import x64.*;
import x64.instructions.MoveInstruction;
import x64.jni.FindClassJNI;
import x64.jni.GetStaticFieldIdJNI;
import x64.jni.GetStaticFieldJNI;
import x64.operands.X64PreservedRegister;

import static x64.operands.PCRelativeData.fromField;

public class GetStaticFieldStatement implements InterStatement, FindClassJNI, GetStaticFieldIdJNI, GetStaticFieldJNI {
	@NotNull private final String className;
	@NotNull private final String fieldName;
	@NotNull private final Register result;
	
	@NotNull private final String fileName;
	private final int line;
	
	/**
	 * Creates a new get static field statement. 
	 * @param className The class's name.
	 * @param fieldName The field's name.
	 * @param register The result register.
	 */
	public GetStaticFieldStatement(@NotNull String className, @NotNull String fieldName, @NotNull Register register,
			@NotNull String fileName, int line) {
		
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
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		// get the field type for the static field.
		InterFile object = JavaCompiler.parseAndCompile(className, fileName, line);
		Types type = object.getStatFieldType(fieldName, fileName, line);
		
		result.setType(type);
		regs.put(result, type);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		if (className.startsWith("java/")) {

			// the flag on the register that is returned that it is a JNI register is based on the type,
			// which is java/* for java types intrinsic to the JVM.
			// When we call methods on the JVM type, will also have to do the split logic, similar to here

			// Step 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
			//    - name is like: java/lang/String
			final X64PreservedRegister classReg = addFindClassJNICall(context, className);

			// Step 2. fieldID = javaEnv -> GetFieldID(JNIEnv *env, class, char *name, char *sig);
			final X64PreservedRegister fieldIDReg =
				addGetStaticFieldIdJNICall(result, fieldName, classReg, context);


			// Step 3. result = javaEnv -> GetStatic<Type>Field(JNIEnv *env, class, fieldID)
			addGetStaticField(context, classReg, fieldIDReg, result);

		} else {
			// mov CLASS_NAME_FIELD_NAME(%rip), %destination
			context.addInstruction(
				new MoveInstruction(
					fromField(className, fieldName, result),
					result.toX64()
				)
			);
		}

	}
}
