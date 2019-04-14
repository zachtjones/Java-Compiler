package x64.allocation;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64PseudoRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RegistersUsed {

	// map of pseudo to the number of times it is used.
	private final Map<X64PseudoRegister, Integer> numberUsages;

	private final Map<X64PseudoRegister, Integer> lastUsage;

	private final Map<X64PseudoRegister, Integer> firstUsage;

	private TreeSet<Integer> functionCallLines;

	/** Creates a mapping of the registers used and defined at which lines. */
	public RegistersUsed(@NotNull HashMap<X64PseudoRegister, Integer> lastReads) {
		functionCallLines = new TreeSet<>();
		numberUsages = new HashMap<>();
		this.lastUsage = lastReads;
		firstUsage = new HashMap<>();
	}

	/** Marks a register as being used at line */
	public void markUsed(X64PseudoRegister used, int line) {
		int mapping = lastUsage.getOrDefault(used, 0);
		lastUsage.put(used, Math.max(mapping, line));

		mapping = firstUsage.getOrDefault(used, Integer.MAX_VALUE);
		firstUsage.put(used, Math.min(mapping, line));

		increment(used);
	}

	/** Marks a register as being defined at line (aka given a value) */
	public void markDefined(X64PseudoRegister defined, int line) {
		markUsed(defined, line);
	}

	/** Increases the number of times that a register is used.
	 * @param reg The register that is either get or set. */
	private void increment(X64PseudoRegister reg) {
		Integer count = numberUsages.getOrDefault(reg, 0);
		numberUsages.put(reg, count + 1);
	}

	/** Marks a function call occurring at the line specified */
	void markFunctionCall(int line) {
		functionCallLines.add(line);
	}

	/** Returns the index of the instruction that last uses the register */
	private int getLastUsage(X64PseudoRegister register) {
		return lastUsage.get(register);
	}

	/** Returns the index of the instruction that last uses the register */
	private int getFirstDefinition(X64PseudoRegister register) {
		return firstUsage.get(register);
	}

	/** Utility method for determining if a register in the function can be temporary */
	boolean canBeTemporary(X64PseudoRegister register) {

		// TODO this will be more complicated with branches backwards

		if (lastUsage.containsKey(register)) {
			int defined = getFirstDefinition(register);
			int lastUsed = getLastUsage(register);

			// it can be temporary if not used across function call
			return functionCallLines.stream().noneMatch(
				callLine -> defined < callLine && lastUsed > callLine
			);
		} else {
			// if it's not used, the register can definitely be temporary
			return true;
		}
	}

	/**
	 * Returns a tree map of the registers in order of priority.
	 * @param mapping The mapping of pseudo registers to their native ones.
	 * @return The mapping.
	 */
	TreeSet<RegisterMapped> prioritize(HashMap<X64PseudoRegister, RegisterMapped> mapping) {
		for (X64PseudoRegister reg : mapping.keySet()) {
			mapping.get(reg).add(numberUsages.get(reg));
		}
		return new TreeSet<>(mapping.values());
	}

	/** Obtains the set of registers that are last read on this line number. */
	Set<X64PseudoRegister> getLastReads(int lineNumber) {
		// TODO pre-compute these so it's not a O(N^2) operation.
		// entry is <Reg, line#>
		return lastUsage.entrySet().stream()
			.filter(i -> i.getValue() == lineNumber) // this line only
			.map(Map.Entry::getKey) // obtain the set of the registers
			.collect(Collectors.toSet());
	}

	/** Obtains the set of registers that are first written to on this line number. */
	Set<X64PseudoRegister> getFirstWrites(int lineNumber) {
		// basically the same as last reads
		return firstUsage.entrySet().stream()
			.filter(i -> i.getValue() == lineNumber)
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());
	}
}
