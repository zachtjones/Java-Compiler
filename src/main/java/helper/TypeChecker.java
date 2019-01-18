package helper;

import intermediate.Register;

public class TypeChecker {
	
	/**
	 * Verifies that you can directly assign source to target.
	 * If the types are the same, source is null and target is a reference,
	 * or if source is a subclass of target, the operation will be allowed.
	 * Due to conversions between primitive types requiring instructions,
	 * you will need to add some convert statements.
	 * @throws CompileException if subclassName is not the same as className or a subclass of it.
	 */
	public static void canDirectlyAssign(Types target, Types source, String fileName, int line)
			throws CompileException {
		
		if (target.equals(source)) {
			return;
		}

		if (source == Types.NULL && !target.isPrimitive()) return;

		// TODO handle inheritance

		throw new CompileException("can't convert directly: " + source + " to: " + target, fileName, line);
	}
	
	/***
	 * Verifies addrType is a pointer and dataType can be stored at that pointer.
	 * If it can't, throws a compileException.
	 * @param dataType The register for the data
	 * @param addrType The register for the address (should be SOMETHING*)
	 */
	public static void checkAssign(Register dataType, Register addrType, String fileName, int line)
			throws CompileException {
		
		// check the type of the address to make sure it's a pointer
		Types resultType = addrType.getType().dereferencePointer(fileName,line);

		if (addrType.isPrimitive()) {
			if (dataType.isPrimitive()) {
				return;
			} else {
				throw new CompileException(dataType + " can't be stored at " + addrType, fileName, line);
			}
		} else {
			if (dataType.isPrimitive()) {
				throw new CompileException(dataType + " can't be stored at " + addrType, fileName, line);
			}
		}
		
		// both are reference types, check subclass or equal
		canDirectlyAssign(resultType, dataType.getType(), fileName, line);
	}
}
