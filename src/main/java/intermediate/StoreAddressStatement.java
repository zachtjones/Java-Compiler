package intermediate;

import conversions.Conversion;
import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.*;
import x64.operands.PseudoDisplacement;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToPseudoDisplacement;
import x64.pseudo.MovePseudoToRIPRelative;

import java.util.HashMap;
import java.util.List;

/** store %src at %addr */
public class StoreAddressStatement implements InterStatement,
		FindClassJNI, GetInstanceFieldIdJNI, SetInstanceFieldJNI,
		GetStaticFieldIdJNI, SetStaticFieldJNI {

	@NotNull private final Register src;
	@NotNull private final Register addr;
	
	@NotNull private final String fileName;
	private final int line;

	// set in type checking, used for converting to the result.
	private Register intermediate;
	private List<InterStatement> conversions;

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

		// this is an assignment, allocate another reg and save the conversion
		Types destinationType = addr.getType().dereferencePointer(fileName, line);
		intermediate = func.allocator.getNext(destinationType);
		conversions = Conversion.assignmentConversion(src, intermediate, fileName, line);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		// compile in the transformation from src to intermediate
		for (InterStatement i : conversions) {
			i.compile(context);
		}

		if (context.registerIsInstanceFieldAddress(addr)) {
			final String fieldName = context.getInstanceFieldAddressName(addr);
			final Register object = context.getInstanceFieldInstance(addr);

			final String className = object.getType().getClassName(fileName, line);
			if (className.startsWith("java/")) {

				// Step 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
				//    - name is like: java/lang/String
				final X64PseudoRegister classReg = addFindClassJNICall(context, className);

				// Step 2. fieldID = javaEnv -> GetFieldID(JNIEnv *env, class, char *name, char *sig);
				// src holds the type
				final X64PseudoRegister fieldIDReg =
					addGetInstanceFieldIdJNICall(intermediate, fieldName, classReg, context);


				// Step 3. javaEnv -> Set<Type>Field(JNIEnv *env, object, fieldID, value)
				addSetInstanceField(context, object, fieldIDReg, intermediate);

			} else {
				// parse and compile the object's class, to obtain the offset
				InterFile objectClass = JavaCompiler.parseAndCompile(
					object.getType().getClassName(fileName, line), fileName, line
				);
				int fieldOffset = objectClass.getFieldOffset(fieldName);

				// mov %src, field_offset(%instance)
				context.addInstruction(
					new MovePseudoToPseudoDisplacement(
						intermediate.toX64(),
						new PseudoDisplacement(fieldOffset, object.toX64())
					)
				);
			}
		} else if (context.registerIsStaticFieldAddress(addr)) {
			final String fieldName = context.getStaticFieldAddressFieldName(addr);
			final String className = context.getStaticFieldAddressClassName(addr);

			if (className.startsWith("java/")) {

				// Step 1. class = javaEnv -> FindClass(JNIEnv *env, char* name);
				final X64PseudoRegister classReg = addFindClassJNICall(context, className);

				// Step 2. fieldID = javaEnv -> GetStaticFieldID(JNIEnv *env, class, char *name, char *sig);
				// src holds the type
				final X64PseudoRegister fieldIDReg =
					addGetStaticFieldIdJNICall(intermediate, fieldName, classReg, context);

				// Step 3. javaEnv -> SetStatic<Type>Field(JNIEnv *env, class, fieldID, value)
				addSetStaticField(context, classReg, fieldIDReg, intermediate);

			} else {
				// mov %src, class_name_field_offset(%rip), src is used in the destination for the data size
				context.addInstruction(
					new MovePseudoToRIPRelative(
						intermediate.toX64(),
						RIPRelativeData.fromField(className, fieldName)
					)
				);
			}
		} else if (context.registerIsLocalRegisterAddress(addr)) {

			// no special logic for JNI, just need to set the register
			//   the local is just an alias

			// this is an assignment to the local variable
			final String localName = context.getLocalAddressLocalName(addr);
			// compile as if putLocal  src @ address
			new PutLocalStatement(src, localName, fileName, line).compile(context);

		} else {
			throw new CompileException("store address statement unknown type", fileName, line);
		}
	}
}
