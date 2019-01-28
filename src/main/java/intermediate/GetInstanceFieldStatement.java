package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import x64.X64Context;
import x64.instructions.MoveInstruction;
import x64.jni.FindClassJNI;
import x64.jni.GetInstanceFieldIdJNI;
import x64.jni.GetInstanceFieldJNI;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64RegisterOperand;

public class GetInstanceFieldStatement implements InterStatement, FindClassJNI, GetInstanceFieldIdJNI, GetInstanceFieldJNI {
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
		
		InterFile object = JavaCompiler.parseAndCompile(type.getClassName(fileName, line), fileName, line);
		Types resultType = object.getInstFieldType(fieldName, fileName, line);
		
		regs.put(result, resultType);
	}

	@Override
	public void compile(X64Context context) throws CompileException {
		final String className = instance.getType().getClassName(fileName, line);
		if (className.startsWith("java/")) {

			// Step 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
			//    - name is like: java/lang/String
			final X64RegisterOperand classReg = addFindClassJNICall(context, className);

			// Step 2. fieldID = javaEnv -> GetFieldID(JNIEnv *env, class, char *name, char *sig);
			// src holds the type
			final X64RegisterOperand fieldIDReg =
				addGetInstanceFieldIdJNICall(instance, fieldName, classReg, context);


			// Step 3. javaEnv -> Get<Type>Field(JNIEnv *env, object, fieldID) -> result
			addGetInstanceField(context, instance, fieldIDReg, result);

		} else {
			// parse and compile the object's class, to obtain the offset
			InterFile objectClass = JavaCompiler.parseAndCompile(
				instance.getType().getClassName(fileName, line), fileName, line
			);
			int fieldOffset = objectClass.getFieldOffset(fieldName);

			// mov field_offset(%instance), result
			context.addInstruction(
				new MoveInstruction(
					new RegisterRelativePointer(fieldOffset, instance.toX64()),
					result.toX64()
				)
			);
		}
	}
}
