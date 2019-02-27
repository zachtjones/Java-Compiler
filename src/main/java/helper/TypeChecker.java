package helper;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static helper.Types.*;

public class TypeChecker {

	private final static HashMap<Types, Types> boxingPairs = new HashMap<>();

	private static void put(Types t1, Types t2) {
		boxingPairs.put(t1, t2);
		boxingPairs.put(t2, t1);
	}

	static {
		put(BOOLEAN, fromFullyQualifiedClass("java/lang/Boolean"));
		put(BYTE, fromFullyQualifiedClass("java/lang/Byte"));
		put(CHAR, fromFullyQualifiedClass("java/lang/Character"));
		put(SHORT, fromFullyQualifiedClass("java/lang/Short"));
		put(INT, fromFullyQualifiedClass("java/lang/Integer"));
		put(LONG, fromFullyQualifiedClass("java/lang/Long"));
		put(FLOAT, fromFullyQualifiedClass("java/lang/Float"));
		put(DOUBLE, fromFullyQualifiedClass("java/lang/Double"));
	}

	/**
	 * Returns if the source can be boxed to destination (int -> Integer, Integer -> int, ...)
	 * @param t1 The first type.
	 * @param t2 The second type.
	 * @return A truth value.
	 */
	public static boolean isBoxingAssignable(@NotNull Types t1, @NotNull Types t2) {
		return boxingPairs.containsKey(t1) || boxingPairs.containsKey(t2);
	}

	/**
	 * Returns if the source can be assigned to destination without any converting operations.
	 * @param source The source type.
	 * @param destination The destination type.
	 * @return a boolean if source can be assigned to destination.
	 */
	public static boolean isDirectlyAssignable(@NotNull Types source, @NotNull Types destination) {
		if (source.equals(destination)) return true;

		return source.equals(Types.NULL) && !destination.isPrimitive();

		// TODO test is source is subclass of destination -- definitely is an instance
	}

	public static boolean isCastAssignable(@NotNull Types source, @NotNull Types destination) {

		return source.isPrimitive() && destination.isPrimitive();

		// TODO test if destination is subclass of source -- could be an instance
	}

	/** If source can't be assigned to destination (even with conversions),
	 *  will throw a CompileException
	 */
	public static void assertCanAssignWithCasts(@NotNull Types source, @NotNull Types destination,
									   @NotNull String fileName, int line) throws CompileException {

		if (!isDirectlyAssignable(source, destination) &&
			!isCastAssignable(source, destination) &&
			!isBoxingAssignable(source, destination)) {

			throw new CompileException("can't convert: " + source + " to: " + destination, fileName, line);
		}
	}

	/** If source can't be converted to destination with either direct or boxing assignment,
	 * throws a CompileException */
	public static void assertCanAssignWithBoxing(@NotNull Types source, @NotNull Types destination,
												@NotNull String fileName, int line) throws CompileException {

		if (!isDirectlyAssignable(source, destination) &&
			!isBoxingAssignable(source, destination)) {

			throw new CompileException("can't convert: " + source + " to: " + destination, fileName, line);
		}
	}

}
