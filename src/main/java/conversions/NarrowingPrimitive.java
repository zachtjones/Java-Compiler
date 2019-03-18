package conversions;

import helper.Types;
import intermediate.InterStatement;
import intermediate.Register;
import intermediate.TruncationStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class NarrowingPrimitive extends Conversion {

	@NotNull private final List<InterStatement> conversion;

	/** Represents a widening conversion of primitives.
	 * Precondition: WideningPrimitive.matches(source.getType(), destination.getType())
	 */
	NarrowingPrimitive(Register source, Register destination) {
		// this is a truncation in all cases, we will let the intermediate stage handle this
		conversion = Collections.singletonList(new TruncationStatement(source, destination));
	}

	@Override
	public @NotNull List<InterStatement> statements() {
		return conversion;
	}

	/** Returns if source can be converted to destination using a primitive widening conversion */
	static boolean matches(Types source, Types destination) {
		// this is actually the exact opposite direction of the conversion
		return WideningPrimitive.matches(destination, source);
	}
}
