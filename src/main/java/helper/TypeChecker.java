package helper;

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

}
