package x64.allocation;

import x64.operands.X64PreservedRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class RegistersUsed {

	// map of 1 register -> many usages
	private Map<X64PreservedRegister, TreeSet<Integer>> usages;

	private Map<X64PreservedRegister, Integer> definition;

	private TreeSet<Integer> functionCallLines;

	public RegistersUsed() {
		usages = new HashMap<>();
		definition = new HashMap<>();
		functionCallLines = new TreeSet<>();
	}

	/** Marks a register as being used at line */
	public void markUsed(X64PreservedRegister used, int line) {
		TreeSet<Integer> mapping = usages.getOrDefault(used, new TreeSet<>());
		mapping.add(line);
		usages.putIfAbsent(used, mapping);
	}

	/** Marks a register as being defined at line (aka given a value) */
	public void markDefined(X64PreservedRegister defined, int line) {
		definition.put(defined, line);
	}

	/** Marks a function call occurring at the line specified */
	void markFunctionCall(int line) {
		functionCallLines.add(line);
	}

	/** Returns the index of the instruction that last uses the register */
	private int getLastUsage(X64PreservedRegister register) {
		return usages.get(register).last();
	}

	/** Utility method for determining if a register in the function can be temporary */
	boolean canBeTemporary(X64PreservedRegister register) {

		// TODO this will be more complicated with branches backwards

		if (usages.containsKey(register)) {
			int defined = definition.get(register);
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
	 * Returns a mapping of line offset to the register that is last used at that line.
	 * There will be no mapping for a line which has no last usages of a register.
	 */
	Map<Integer, X64PreservedRegister> getLastUsages() {
		HashMap<Integer, X64PreservedRegister> used = new HashMap<>();
		usages.forEach((register, integers) -> used.put(integers.last(), register));
		return used;
	}

	/**
	 * Returns a mapping of the line offset to the register that is defined at that line.
	 * There will be no mapping for a line which has no definition of a register.
	 */
	Map<Integer, X64PreservedRegister> getDefinitions() {
		HashMap<Integer, X64PreservedRegister> temp = new HashMap<>();
		definition.forEach(((x64PreservedRegister, integer) -> temp.put(integer, x64PreservedRegister)));
		return temp;
	}
}
