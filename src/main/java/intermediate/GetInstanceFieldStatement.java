package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.FindClassJNI;
import x64.jni.GetArrayLengthJNI;
import x64.jni.GetInstanceFieldIdJNI;
import x64.jni.GetInstanceFieldJNI;
import x64.operands.PseudoDisplacement;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoDisplacementToPseudo;

import java.util.HashMap;

public class GetInstanceFieldStatement implements InterStatement, FindClassJNI, GetInstanceFieldIdJNI,
	GetInstanceFieldJNI, GetArrayLengthJNI {

	@NotNull private Register instance;
	@NotNull private String fieldName;
	
	@NotNull private Register result;
	
	@NotNull private final String fileName;
	private final int line;

	private boolean isArrayLengthAccess;
	
	/**
	 * Creates a new get static field statement. 
	 * @param instance The instance's register holding it's value.
	 * @param fieldName The field's name.
	 */
	public GetInstanceFieldStatement(@NotNull Register instance, @NotNull String fieldName, @NotNull Register result,
									 @NotNull String fileName, int line) {
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
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {

		UsageCheck.verifyDefined(instance, regs, fileName, line);
		// the type of the object
		Types type = instance.getType();

		isArrayLengthAccess = type.isArrayType();
		if (isArrayLengthAccess) {
			// if the object is an array, only field accessible is .length
			if (!fieldName.equals("length")) {
				throw new CompileException("The length field is the only one defined on arrays.", fileName, line);
			}

			// result type is an int
			result.setType(Types.INT);
			regs.put(result, Types.INT);

		} else {

			// parse and compile the object's class to obtain the result type.
			InterFile object = JavaCompiler.parseAndCompile(type.getClassName(fileName, line), fileName, line);
			Types resultType = object.getInstFieldType(fieldName, fileName, line);

			result.setType(resultType);
			regs.put(result, resultType);
		}
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		if (isArrayLengthAccess) {
			// always JNI; int GetArrayLength(JNIEnv*, array)

			addGetArrayLength(context, instance.toX64(), result.toX64());

			return;
		}

		// it's a field access
		final String className = instance.getType().getClassName(fileName, line);
		if (className.startsWith("java/")) {

			// Step 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
			//    - name is like: java/lang/String
			final X64PseudoRegister classReg = addFindClassJNICall(context, className);

			// Step 2. fieldID = javaEnv -> GetFieldID(JNIEnv *env, class, char *name, char *sig);
			// src holds the type
			final X64PseudoRegister fieldIDReg =
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
				new MovePseudoDisplacementToPseudo(
					new PseudoDisplacement(fieldOffset, instance.toX64()),
					result.toX64()
				)
			);
		}
	}
}
