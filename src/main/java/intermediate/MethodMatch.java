package intermediate;

import helper.Types;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/** Data class used for the information required when calling functions. */
public class MethodMatch implements Comparable<MethodMatch> {
	final @NotNull List<List<InterStatement>> conversionsToArgs;
	final @NotNull InterFunction match;

	MethodMatch(@NotNull List<List<InterStatement>> conversionsToArgs, @NotNull InterFunction match) {
		this.conversionsToArgs = conversionsToArgs;
		this.match = match;
	}

	@Override
	public int compareTo(@NotNull MethodMatch o) {
		// pick which method is the 'simplest' (smallest primitives), or least conversions
		int total = 0;
		for (int i = 0; i < match.paramTypes.size(); i++) {
			Types thisType = match.paramTypes.get(i);
			Types otherType = o.match.paramTypes.get(i);
			total += thisType.compareTo(otherType);
		}
		return total;
	}
}
