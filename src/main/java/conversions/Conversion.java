package conversions;

import helper.CompileException;
import helper.Types;
import intermediate.InterStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Conversion {

	/***
	 * Represents an assignment conversion, used when assigning to a variable (param, local, or field)
	 * @param source The source intermediate register.
	 * @param destination The destination register.
	 * @param fileName The filename of the assignment.
	 * @param line The current line of the assignment.
	 * @return The list of statements needed to assign source to destination.
	 * @throws CompileException If there none of the allowed ways works.
	 */
	public static List<InterStatement> assignmentConversion(@NotNull Register source, @NotNull Register destination,
															@NotNull String fileName, int line)
		throws CompileException {

		Types sourceType = source.getType(), destinationType = destination.getType();

		// identity conversion
		if (IdentityConversion.matches(sourceType, destinationType)) {
			return new IdentityConversion(source, destination).statements();
		}
		// widening primitive conversion
		if (WideningPrimitive.matches(sourceType, destinationType)) {
			return new WideningPrimitive(source, destination).statements();
		}
		// TODO widening reference conversion

		// boxing conversion
		if (BoxingConversion.matches(sourceType, destinationType)) {
			return new BoxingConversion(source, destination).statements();
		}

		// TODO unboxing conversion
		// TODO also could have narrowing conversion to byte, char, short, from a literal on the other side

		throw new CompileException("Conversion not allowed for the assignment "
			+ sourceType + " -> " + destinationType, fileName, line);
	}

	/***
	 * Represents an method invocation conversion, used when assigning to a parameter in a method call.
	 * @param source The source intermediate register.
	 * @param destination The destination register.
	 * @param fileName The filename of the method call.
	 * @param line The current line of the method call.
	 * @return The list of statements needed to assign source to destination.
	 * @throws CompileException If there none of the allowed ways works.
	 */
	public static List<InterStatement> methodInvocation(@NotNull Register source, @NotNull Register destination,
														@NotNull String fileName, int line)
		throws CompileException {

		Types sourceType = source.getType(), destinationType = destination.getType();

		// identity conversion
		if (IdentityConversion.matches(sourceType, destinationType)) {
			return new IdentityConversion(source, destination).statements();
		}
		// widening primitive conversion
		if (WideningPrimitive.matches(sourceType, destinationType)) {
			return new WideningPrimitive(source, destination).statements();
		}
		// TODO widening reference conversion
		// TODO boxing conversion, optionally followed by widening reference
		//   -- do the boxing, anyways, then do a widening if possible
		//   -- example: int -> Number

		// TODO unboxing conversion, optionally followed by widening primitives

		throw new CompileException("Conversion not allowed for the assignment", fileName, line);
	}

	/***
	 * Represents a cast conversion, used when there is an explicit cast.
	 * (Note unlike C, not all casts are allowed.)
	 * @param source The source intermediate register.
	 * @param destination The destination register.
	 * @param fileName The filename of the method call.
	 * @param line The current line of the method call.
	 * @return The list of statements needed to assign source to destination.
	 * @throws CompileException If there none of the allowed ways works.
	 */
	public static List<InterStatement> castingConversion(@NotNull Register source, @NotNull Register destination,
														 @NotNull String fileName, int line)
		throws CompileException {

		Types sourceType = source.getType(), destinationType = destination.getType();

		// identity conversion
		if (IdentityConversion.matches(sourceType, destinationType)) {
			return new IdentityConversion(source, destination).statements();
		}
		// widening primitive conversion
		if (WideningPrimitive.matches(sourceType, destinationType)) {
			return new WideningPrimitive(source, destination).statements();
		}
		// narrowing primitive conversion  (example float -> int)
		if (NarrowingPrimitive.matches(sourceType, destinationType)) {
			return new NarrowingPrimitive(source, destination).statements();
		}

		// TODO widening and narrowing primitive conversion

		// TODO widening reference conversion,
		//  -- optionally followed by either an unboxing conversion or an unchecked conversion

		// TODO narrowing reference conversion,
		//  -- optionally followed by either an unboxing conversion or an unchecked conversion

		// TODO boxing conversion, optionally followed by a widening reference conversion

		// TODO unboxing conversion, optionally followed by a widening primitive conversion


		throw new CompileException("Conversion not allowed for the assignment", fileName, line);
	}

	// TODO more ways, see https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.1.9

	/** Returns a list of intermediate statements that will convert from source to destination. */
	@NotNull
	public abstract List<InterStatement> statements();
}
