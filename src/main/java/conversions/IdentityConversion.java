package conversions;

import helper.Types;
import intermediate.CopyStatement;
import intermediate.InterStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class IdentityConversion extends Conversion {

	@NotNull private final List<InterStatement> conversion;

	/** Represents just a simple copy from source to destination.
	 * Precondition: IdentityConversion.matches(source.getType(), destination.getType())
	 */
	IdentityConversion(Register source, Register destination) {
		conversion = Collections.singletonList(new CopyStatement(source, destination, "", -1));
	}

	@Override
	public @NotNull List<InterStatement> statements() {
		return conversion;
	}

	/** Returns if source is equal to destination. */
	static boolean matches(Types source, Types destination) {
		return source.equals(destination);
	}
}
