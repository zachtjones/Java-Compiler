package helper;

import intermediate.Register;

public class TypeChecker {
	
	/**
	 * Verifies that subclassName is a subclass or the same as className.
	 * @throws CompileException if subclassName is not the same as className or a subclass of it.
	 */
	public static boolean subclassOrEqual(String className, String subclassName) throws CompileException {
		if (className.equals(subclassName)) {
			return true;
		}
		// TODO use the file system and perform lookups and such
		return false;
	}
	
	/***
	 * Verifies addrType is a pointer and dataType can be stored at that pointer.
	 * If it can't, throws a compileException.
	 * @param dataType The register for the data
	 * @param addrType The register for the address (should be SOMETHING*)
	 */
	public static void checkAssign(Register dataType, Register addrType) throws CompileException {
		// check the type of the address to make sure it's a pointer
		if (!addrType.typeFull.endsWith("*")) {
			throw new CompileException(addrType + " is not a pointer type");
		}
		
		String pointerType = addrType.typeFull.substring(0, addrType.typeFull.length() - 1);
		
		if (addrType.isPrimitive()) {
			if (dataType.isPrimitive()) {
				return;
			} else {
				// TODO check unboxing
				throw new CompileException(dataType + " can't be stored at " + addrType);
			}
		} else {
			if (dataType.isPrimitive()) {
				// TODO check boxing
				throw new CompileException(dataType + " can't be stored at " + addrType);
			}
		}
		
		// both are reference types, check subclass or equal
		if (!subclassOrEqual(pointerType, dataType.typeFull)) {
			throw new CompileException(dataType.typeFull + " can't be converted to: " + pointerType);
		}
	}
}
