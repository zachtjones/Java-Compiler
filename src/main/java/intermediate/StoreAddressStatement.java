package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import helper.UsageCheck;
import x64.X64File;
import x64.X64Function;
import x64.instructions.MoveInstruction;
import x64.jni.FindClassJNI;
import x64.jni.GetInstanceFieldIdJNI;
import x64.jni.SetInstanceFieldJNI;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64RegisterOperand;

/** store %src at %addr */
public class StoreAddressStatement implements InterStatement, FindClassJNI, GetInstanceFieldIdJNI, SetInstanceFieldJNI {
	private Register src;
	private Register addr;
	
	private final String fileName;
	private final int line;
	
	public StoreAddressStatement(Register src, Register addr, String fileName, int line) {
		this.src = src;
		this.addr = addr;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "store " + src.toString() + " at " + addr.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(addr, regs, fileName, line);
		UsageCheck.verifyDefined(src, regs, fileName, line);
		
		TypeChecker.canDirectlyAssign(src.getType(), addr.getType().dereferencePointer(fileName, line), fileName, line);
	}

	@Override
	public void compile(X64File assemblyFile, X64Function function) throws CompileException {
		if (function.registerIsInstanceFieldAddress(addr)) {
			final String fieldName = function.getInstanceFieldAddressName(addr);
			final Register object = function.getInstanceFieldInstance(addr);

			final String className = object.getType().getClassName(fileName, line);
			if (className.startsWith("java/")) {

				// Step 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
				//    - name is like: java/lang/String
				final X64RegisterOperand classReg = addFindClassJNICall(assemblyFile, function, className);

				// Step 2. fieldID = javaEnv -> GetFieldID(JNIEnv *env, class, char *name, char *sig);
				// src holds the type
				final X64RegisterOperand fieldIDReg =
					addGetInstanceFieldIdJNICall(src, fieldName, classReg, assemblyFile, function);


				// Step 3. javaEnv -> Set<Type>Field(JNIEnv *env, object, fieldID, value)
				addSetInstanceField(function, object, fieldIDReg, src);

			} else {
				// mov %src, field_offset(%instance)

				function.addInstruction(
					new MoveInstruction(
						src.toX64(),
						new RegisterRelativePointer(function.getFieldOffset(fieldName), object)
					)
				);
			}
		} else {
			throw new CompileException("store address statement unknown type", fileName, line);
		}
	}
}
