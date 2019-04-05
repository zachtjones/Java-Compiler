package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.GetObjectArrayElementJNI;
import x64.jni.GetPrimitiveArrayElements;
import x64.jni.ReleasePrimitiveArrayElements;
import x64.operands.PseudoIndexing;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MoveArrayIndexToPseudo;

import java.util.HashMap;

public class GetArrayValueStatement implements InterStatement,
	GetPrimitiveArrayElements, ReleasePrimitiveArrayElements,
	GetObjectArrayElementJNI {
	
	@NotNull private final Register array, index, result;
	
	@NotNull private final String fileName;
	private final int line;

	/**
	 * Represents getting a value out of an array.
	 */
	public GetArrayValueStatement(@NotNull Register array, @NotNull Register index, @NotNull Register result,
								  @NotNull String fileName, int line) {
		this.array = array;
		this.index = index;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}

	public String toString() {
		return "getArrayValue " + result + " = " + array + " @ " + index + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(array, regs, fileName, line);
		UsageCheck.verifyDefined(index, regs, fileName, line);
		
		// make sure the type of the array ends in []
		Types resultingType = array.getType().removeArray(fileName, line);

		// fill in the result's type
		result.setType(resultingType);
		regs.put(result, resultingType);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		// note that this is NOT an assignment to the result. The conversion
		//  is not needed here, it's the the result register is used there could be a need for a conversion.

		final Types destinationType = result.getType();

		// if the source array isn't a primitive array, simple just get object array element
		if (destinationType.isPrimitive()) {
			// buffer = Get<PrimitiveType>ArrayElements(JNIEnv *env, array, jboolean* isCopy)
			//   isCopy can be used to determine if it's able to make a copy, or actually memory map it
			//   can just pass null in instead, as we're going to do the release anyways
			X64PseudoRegister buffer = addGetPrimitiveArrayElements(context, array, destinationType);

			//  get the memory at the buffer+index*scaling
			// mov (%baseReg, %indexReg, scale factor), %result
			context.addInstruction(
				new MoveArrayIndexToPseudo(
					new PseudoIndexing(buffer, index.toX64(), destinationType.byteSize()),
					result.toX64()
				)
			);

			// Release<PrimitiveType>ArrayElements(JNIEnv *env, array, void* elements, int mode)
			//   mode should always be 0, want to copy back the content and free the buffer
			addReleasePrimitiveArrayElements(context, array, buffer, destinationType);

		} else {
			// object GetObjectArrayElement(JNIEnv *env, jobjectArray array, jsize index)
			addGetObjectArrayElement(context, array, index, result);
		}
	}
}
