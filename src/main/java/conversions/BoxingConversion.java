package conversions;

import helper.Types;
import intermediate.InterStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static helper.Types.*;

/** Represents a conversion from primitive to their representative class type. */
public class BoxingConversion extends Conversion {

	private final static HashMap<Types, Types> boxingPairs = new HashMap<>();

	static {
		boxingPairs.put(BOOLEAN, fromFullyQualifiedClass("java/lang/Boolean"));
		boxingPairs.put(BYTE, fromFullyQualifiedClass("java/lang/Byte"));
		boxingPairs.put(CHAR, fromFullyQualifiedClass("java/lang/Character"));
		boxingPairs.put(SHORT, fromFullyQualifiedClass("java/lang/Short"));
		boxingPairs.put(INT, fromFullyQualifiedClass("java/lang/Integer"));
		boxingPairs.put(LONG, fromFullyQualifiedClass("java/lang/Long"));
		boxingPairs.put(FLOAT, fromFullyQualifiedClass("java/lang/Float"));
		boxingPairs.put(DOUBLE, fromFullyQualifiedClass("java/lang/Double"));
	}

	@NotNull private final List<InterStatement> statements;

	public BoxingConversion(Register source, Register destination) {

		// use destination = Type.valueOf(source)
		// TODO -- might also need an allocator here for intermediate steps.
		throw new RuntimeException("not implemented yet, boxing conversions.");
	}

	@Override
	public @NotNull List<InterStatement> statements() {
		return this.statements;
	}

	/** Returns true if and only if source is a primitive and destination is the corresponding
	 * class for boxing. */
	public static boolean matches(Types source, Types destination) {
		// source primitive to the matching class
		return boxingPairs.containsKey(source) &&
			boxingPairs.get(source).equals(destination);
	}
}
