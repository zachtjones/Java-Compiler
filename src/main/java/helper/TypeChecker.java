package helper;

import intermediate.Register;

public class TypeChecker {
	
	/**
	 * Verifies that you can assign source to target.
	 * @throws CompileException if subclassName is not the same as className or a subclass of it.
	 */
	public static void canAssign(Types target, Types source, String fileName, int line)
			throws CompileException {
		
		if (target.equals(source)) {
			return;
		}
		// null can be assigned to any reference type
		if (source.equals("null") && !isPrimitive(target)) {
			return;
		}
		
		// do the java widening conversions
		if (isPrimitive(source) && isPrimitive(target)) {
			switch(source) {
			case "byte":
				switch(target) {
				case "short":
				case "int":
				case "long":
				case "float":
				case "double":
					return;
				}
			case "short":
				switch(target) {
				case "int":
				case "long":
				case "float":
				case "double":
					return;
				}
			case "char":
				switch(target) {
				case "int":
				case "long":
				case "float":
				case "double":
					return;
				}
			case "int":
				switch(target) {
				case "long":
				case "float":
				case "double":
					return;
				}
			case "long":
				switch(target) {
				case "float":
				case "double":
					return;
				}
			case "float":
				switch(target) {
				case "double":
					return;
				}
			}
		}
		
		// handle boxing / un-boxing
		
		// TODO use the file system and perform lookups and such

		throw new CompileException("can't convert: " + source + " to: " + target, fileName, line);
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
		if (!addrType.typeFull.endsWith("*")) {
			throw new CompileException(addrType + " is not a pointer type", fileName, line);
		}
		
		String pointerType = addrType.typeFull.substring(0, addrType.typeFull.length() - 1);
		
		if (addrType.isPrimitive()) {
			if (dataType.isPrimitive()) {
				return;
			} else {
				// TODO check unboxing
				throw new CompileException(dataType + " can't be stored at " + addrType, fileName, line);
			}
		} else {
			if (dataType.isPrimitive()) {
				// TODO check boxing
				throw new CompileException(dataType + " can't be stored at " + addrType, fileName, line);
			}
		}
		
		// both are reference types, check subclass or equal
		canAssign(pointerType, dataType.typeFull, fileName, line);
	}
	
	/** Returns true if the value is a primitive type. */
	private static boolean isPrimitive(String value) {
		switch(value) {
		case "boolean":
		case "byte":
		case "char":
		case "short":
		case "int":
		case "long":
		case "float":
		case "double":
			return true;
		default:
			return false;
		}
	}
}
