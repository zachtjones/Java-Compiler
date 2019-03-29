package intermediate;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.FindClassJNI;
import x64.jni.JNIOffsets;
import x64.jni.NewObjectArrayJNI;
import x64.jni.NewPrimitiveArrayJNI;
import x64.operands.X64PseudoRegister;

import java.util.HashMap;

public class CreateArrayStatement implements InterStatement, FindClassJNI, NewPrimitiveArrayJNI, NewObjectArrayJNI {

	@NotNull private Register size;
	@NotNull private final Types type; // the type of the elements, not the array type.
	@NotNull private final Register result;
	@NotNull private final String filename;
	private final int line;

	/**
	 * A statement that creates an array of the type specified.
	 * @param size The number of elements in the array.
	 * @param type The type of the elements the array contains.
	 * @param result The Register that should hold the result of the creation.
	 */
	public CreateArrayStatement(@NotNull Register size, @NotNull Types type, @NotNull Register result,
								@NotNull String filename, int line) {
		this.size = size;
		this.type = type;
		this.result = result;
		this.filename = filename;
		this.line = line;
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		result.setType(Types.arrayOf(type));
		regs.put(result, result.getType());
	}

	@Override
	public String toString() {
		return "createArray " + result + " = new " + type + "[" + size + "]";
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		// always JNI, use either the newObjectArray or newPrimitiveArray
		// args are different based on whether it's a class or the primitive array

		JNIOffsets offset = JNIOffsets.getCreateArrayOffset(type);

		if (type.isPrimitive()) {
			// new<PrimitiveType>Array(JNI*, length) -- distinguished by the offset into the function v-table
			addNewPrimitiveArrayJNI(context, offset, size, result);

		} else {
			// findClass(name)
			final String className = result.getType().getClassName(filename, line);
			final X64PseudoRegister clazz = addFindClassJNICall(context, className);

			// newObjectArray(JNI*, length, elementClass, initialElement)
			addNewObjectArrayJNI(context, clazz, size, result);
		}
	}
}
