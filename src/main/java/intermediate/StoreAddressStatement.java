package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.*;
import x64.operands.RIPRelativeData;
import x64.operands.PseudoDisplacement;
import x64.operands.X64PreservedRegister;
import x64.pseudo.MovePseudoToRIPRelative;

/** store %src at %addr */
public class StoreAddressStatement implements InterStatement,
		FindClassJNI, GetInstanceFieldIdJNI, SetInstanceFieldJNI,
		GetStaticFieldIdJNI, SetStaticFieldJNI {

	@NotNull private final Register src;
	@NotNull private final Register addr;
	
	@NotNull private final String fileName;
	private final int line;

	public StoreAddressStatement(@NotNull Register src, @NotNull Register addr,
								 @NotNull String fileName, int line) {
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
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(addr, regs, fileName, line);
		UsageCheck.verifyDefined(src, regs, fileName, line);
		
		TypeChecker.canDirectlyAssign(src.getType(), addr.getType().dereferencePointer(fileName, line), fileName, line);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		if (context.registerIsInstanceFieldAddress(addr)) {
			final String fieldName = context.getInstanceFieldAddressName(addr);
			final Register object = context.getInstanceFieldInstance(addr);

			final String className = object.getType().getClassName(fileName, line);
			if (className.startsWith("java/")) {

				// Step 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
				//    - name is like: java/lang/String
				final X64PreservedRegister classReg = addFindClassJNICall(context, className);

				// Step 2. fieldID = javaEnv -> GetFieldID(JNIEnv *env, class, char *name, char *sig);
				// src holds the type
				final X64PreservedRegister fieldIDReg =
					addGetInstanceFieldIdJNICall(src, fieldName, classReg, context);


				// Step 3. javaEnv -> Set<Type>Field(JNIEnv *env, object, fieldID, value)
				addSetInstanceField(context, object, fieldIDReg, src);

			} else {
				// parse and compile the object's class, to obtain the offset
				InterFile objectClass = JavaCompiler.parseAndCompile(
					object.getType().getClassName(fileName, line), fileName, line
				);
				int fieldOffset = objectClass.getFieldOffset(fieldName);

				// mov %src, field_offset(%instance)
				context.addInstruction(
					new MovePseudoToPseudoDisplacement(
						src.toX64(),
						new PseudoDisplacement(fieldOffset, object.toX64())
					)
				);
			}
		} else if (context.registerIsStaticFieldAddress(addr)) {
			final String fieldName = context.getStaticFieldAddressFieldName(addr);
			final String className = context.getStaticFieldAddressClassName(addr);

			if (className.startsWith("java/")) {

				// Step 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
				final X64PreservedRegister classReg = addFindClassJNICall(context, className);

				// Step 2. fieldID = javaEnv -> GetStaticFieldID(JNIEnv *env, class, char *name, char *sig);
				// src holds the type
				final X64PreservedRegister fieldIDReg =
					addGetStaticFieldIdJNICall(src, fieldName, classReg, context);

				// Step 3. javaEnv -> SetStatic<Type>Field(JNIEnv *env, class, fieldID, value)
				addSetStaticField(context, classReg, fieldIDReg, src);

			} else {
				// mov %src, class_name_field_offset(%rip), src is used in the destination for the data size
				context.addInstruction(
					new MovePseudoToRIPRelative(
						src.toX64(),
						RIPRelativeData.fromField(className, fieldName)
					)
				);
			}
		} else {
			throw new CompileException("store address statement unknown type", fileName, line);
		}
	}
}
