package x64.allocation;

import x64.operands.X64PseudoRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class RegistersUsed {

	// map of 1 register -> many gets
	private Map<X64PseudoRegister, TreeSet<Integer>> gets;

	private Map<X64PseudoRegister, TreeSet<Integer>> sets;

	// map of pseudo to the number of times it is used.
	private Map<X64PseudoRegister, Integer> numberUsages;

	private TreeSet<Integer> functionCallLines;

	public RegistersUsed() {
		gets = new HashMap<>();
		sets = new HashMap<>();
		functionCallLines = new TreeSet<>();
		numberUsages = new HashMap<>();
	}

	/** Marks a register as being used at line */
	public void markUsed(X64PseudoRegister used, int line) {
		TreeSet<Integer> mapping = gets.getOrDefault(used, new TreeSet<>());
		mapping.add(line);
		gets.putIfAbsent(used, mapping);
		updateUsage(used);
	}

	/** Marks a register as being defined at line (aka given a value) */
	public void markDefined(X64PseudoRegister defined, int line) {
		TreeSet<Integer> mapping = sets.getOrDefault(defined, new TreeSet<>());
		mapping.add(line);
		sets.putIfAbsent(defined, mapping);
		updateUsage(defined);
	}

	/** Increases the number of times that a register is used.
	 * @param reg The register that is either get or set. */
	private void updateUsage(X64PseudoRegister reg) {
		Integer count = numberUsages.getOrDefault(reg, 0);
		count++;
		numberUsages.put(reg, count);
	}

	/** Marks a function call occurring at the line specified */
	void markFunctionCall(int line) {
		functionCallLines.add(line);
	}

	/** Returns the index of the instruction that last uses the register */
	private int getLastUsage(X64PseudoRegister register) {
		return gets.get(register).last();
	}

	/** Returns the index of the instruction that last uses the register */
	private int getFirstDefinition(X64PseudoRegister register) {
		return sets.get(register).first();
	}

	/** Utility method for determining if a register in the function can be temporary */
	boolean canBeTemporary(X64PseudoRegister register) {

		// TODO this will be more complicated with branches backwards

		if (gets.containsKey(register)) {
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
	 * Returns a mapping of line offset to the register that is last used at that line.
	 * There will be no mapping for a line which has no last gets of a register.
	 */
	Map<Integer, X64PseudoRegister> getLastUsages() {
		HashMap<Integer, X64PseudoRegister> used = new HashMap<>();
		gets.forEach((register, integers) -> used.put(integers.last(), register));
		return used;
	}

	/**
	 * Returns a mapping of the line offset to the register that is defined at that line.
	 * There will be no mapping for a line which has no sets of a register.
	 */
	Map<Integer, X64PseudoRegister> getDefinitions() {
		HashMap<Integer, X64PseudoRegister> temp = new HashMap<>();
		sets.forEach(((register, integers) -> temp.put(integers.first(), register)));
		return temp;
	}

	public TreeSet<RegisterMapped> prioritize(HashMap<X64PseudoRegister, RegisterMapped> mapping) {
		for (X64PseudoRegister reg : mapping.keySet()) {
			mapping.get(reg).add(numberUsages.get(reg));
		}
		return new TreeSet<>(mapping.values());
	}
}
