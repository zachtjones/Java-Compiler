package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.*;
import x64.operands.PseudoDisplacement;
import x64.operands.PseudoIndexing;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.pseudo.*;

import java.util.HashMap;
import java.util.List;

import static conversions.Conversion.assignmentConversion;

/** store %src at %addr */
public class StoreAddressStatement implements InterStatement,
		FindClassJNI, GetInstanceFieldIdJNI, SetInstanceFieldJNI,
		GetStaticFieldIdJNI, SetStaticFieldJNI,
	SetObjectArrayElementJNI, GetPrimitiveArrayElements, ReleasePrimitiveArrayElements {

	@NotNull private final Register src;
	@NotNull private final Register addr;
	
	@NotNull private final String fileName;
	private final int line;

	// set in type checking, used for converting to the result.
	private Register intermediate;
	private List<InterStatement> conversions;
	private Types destinationType;

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
		destinationType = addr.getType().dereferencePointer(fileName, line);
		intermediate = func.allocator.getNext(destinationType);
		conversions = assignmentConversion(src, intermediate, fileName, line);
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

			// move the intermediate result to the final part
			context.addInstruction(
				new MovePseudoToPseudo(
					intermediate.toX64(),
					context.getLocalVariable(localName)
				)
			);
		} else if (context.registerIsArrayValueAddress(addr)) {

			X64Context.Pair<Register, Register> x = context.getRegisterArrayAndIndex(addr);
			// store intermediate at the array[index]
			// have to see if the array is a primitive array or not
			final Register array = x.first;
			final Register index = x.second;

			// assignment conversion to int
			final Register indexConverted = context.getNextILRegister(Types.INT);
			List<InterStatement> conversions = assignmentConversion(index, indexConverted, fileName, line);
			for (InterStatement s : conversions) {
				s.compile(context);
			}

			if (destinationType.isPrimitive()) {
				// promotion to 64 bit value via sign extension, required for the indexing operation
				final X64PseudoRegister indexTo64 = context.getNextQuadRegister();
				context.addInstruction(
					new SignExtendPseudoToPseudo(
						indexConverted.toX64(),
						indexTo64
					)
				);

				// buffer = Get<PrimitiveType>ArrayElements(JNIEnv *env, array, jboolean* isCopy)
				//   isCopy can be used to determine if it's able to make a copy, or actually memory map it
				//   can just pass null in instead, as we're going to do the release anyways
				X64PseudoRegister buffer = addGetPrimitiveArrayElements(context, array, intermediate.getType());

				//  set the memory at the buffer+index*scaling
				// mov %source, (%baseReg, %indexReg, scale factor)
				context.addInstruction(
					new MovePseudoToArrayIndex(
						intermediate.toX64(),
						new PseudoIndexing(buffer, indexTo64, destinationType.byteSize())
					)
				);

				// Release<PrimitiveType>ArrayElements(JNIEnv *env, array, void* elements, int mode)
				//   mode should always be 0, want to copy back the content and free the buffer
				addReleasePrimitiveArrayElements(context, array, buffer, destinationType);

			} else {
				// void SetObjectArrayElement(JNIEnv *env, jobjectArray array, jsize index, jobject value)
				addSetObjectArrayElement(context, array, indexConverted, intermediate);
			}

		} else {
			throw new CompileException("store address statement unknown type", fileName, line);
		}
	}
}
