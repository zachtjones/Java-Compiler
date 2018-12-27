package intermediate;

import java.util.HashMap;

import helper.CompileException;
import main.JavaCompiler;
import x64.*;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.jni.FindClassJNI;
import x64.jni.GetStaticFieldIdJNI;
import x64.operands.PCRelativeData;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import static x64.JNIOffsets.GET_STATIC_FIELD_ID;
import static x64.operands.PCRelativeData.fromField;
import static x64.operands.X64PreservedRegister.fromILRegister;

public class GetStaticFieldStatement implements InterStatement, FindClassJNI, GetStaticFieldIdJNI {
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
	public void compile(X64File assemblyFile, X64Function function) throws CompileException {
		if (className.startsWith("java/")) {

			// the flag on the register that is returned that it is a JNI register is based on the type,
			// which is java/* for java types intrinsic to the JVM.
			// When we call methods on the JVM type, will also have to do the split logic, similar to here

			// Steps:
			// 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
			//    - name is like: java/lang/String for java.lang.String or [Ljava/lang/Object; for java.lang.Object[]
			//    - here the name is just the className that was set

			final X64PreservedRegister classReg = addFindClassJNICall(assemblyFile, function, className);

			// Step 2. fieldID = javaEnv -> GetFieldID(JNIEnv *env, class, char *name, char *sig);
			//    - use the class param just obtained, name is the field name, sig is the field type signature

			final X64PreservedRegister fieldIDReg =
				addGetStaticFieldIdJNICall(result, fieldName, classReg, assemblyFile, function);


			// Step 3. result = javaEnv -> GetStatic<Type>Field(JNIEnv *env, class, fieldID)
			final String fieldType = SymbolNames.getJNISignatureFromILType(result.typeFull);

			// load the args
			function.loadJNI1();
			function.addInstruction(
				new MoveInstruction(
					classReg,
					X64NativeRegister.RSI
				)
			);
			function.addInstruction(
				new MoveInstruction(
					fieldIDReg,
					X64NativeRegister.RDX
				)
			);

			// mov GetFieldID_Offset(%javaEnvOne), %temp3
			final X64PreservedRegister temp3 = X64PreservedRegister.newTempQuad(function.getNextFreeRegister());
			function.addInstruction(
				new MoveInstruction(
					new RegisterRelativePointer(JNIOffsets.getStaticFieldOffset(fieldType), function.javaEnvPointer),
					temp3
				)
			);

			// call *%temp3
			function.addInstruction(
				new CallFunctionPointerInstruction(
					temp3
				)
			);

			// mov %result, %final result
			function.addInstruction(
				new MoveInstruction(
					X64NativeRegister.RAX,
					X64PreservedRegister.fromILRegister(result)
				)
			);


		} else {
			// mov CLASS_NAME_FIELD_NAME(%rip), %destination
			function.addInstruction(
				new MoveInstruction(
					fromField(className, fieldName, result),
					fromILRegister(result)
				)
			);
		}

	}
}
