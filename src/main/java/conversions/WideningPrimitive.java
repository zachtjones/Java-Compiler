package conversions;

import helper.Types;
import intermediate.InterStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static helper.Types.*;

public class WideningPrimitive extends Conversion {

	@NotNull private final List<InterStatement> conversion;

	/** Represents a widening conversion of primitives.
	 * Precondition: WideningPrimitive.matches(source.getType(), destination.getType())
	 */
	WideningPrimitive(Register source, Register destination) {
		conversion = new LinkedList<>();

		if (source.getType().equals(CHAR)) { // char is the only unsigned type, zero extend
			return Collections.singletonList(new ZeroExtendStatement(source, destination));
		} else { // sign extend
			return Collections.singletonList(new SignExtendStatement(source, destination));
		}
	}

	@Override
	public @NotNull List<InterStatement> statements() {
		return conversion;
	}

	/** Returns if source can be converted to destination using a primitive widening conversion */
	static boolean matches(Types source, Types destination) {
		// byte to short, int, long, float, or double
		if (source.equals(BYTE)) {
			return destination.equals(SHORT) || destination.equals(INT) ||
				destination.equals(LONG) || destination.equals(FLOAT) || destination.equals(DOUBLE);
		}

		// short to int, long, float, or double
		// char to int, long, float, or double
		if (source.equals(SHORT) || source.equals(CHAR)) {
			return destination.equals(INT) || destination.equals(LONG) ||
				destination.equals(FLOAT) || destination.equals(DOUBLE);
		}

		// int to long, float, or double
		if (source.equals(INT)) {
			return destination.equals(LONG) || destination.equals(FLOAT) || destination.equals(DOUBLE);
		}

		// long to float or double
		if (source.equals(LONG)) {
			return destination.equals(FLOAT) || destination.equals(DOUBLE);
		}

		// float to double
		if (source.equals(FLOAT)) {
			return destination.equals(DOUBLE);
		}

		return false;
	}
}
