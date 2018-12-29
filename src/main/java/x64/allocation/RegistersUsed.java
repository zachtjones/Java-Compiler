package x64.allocation;

import x64.operands.X64PreservedRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class RegistersUsed {

	// map of 1 register -> many usages
	private Map<X64PreservedRegister, TreeSet<Integer>> usages;

	private Map<X64PreservedRegister, Integer> definition;

	public RegistersUsed() {
		usages = new HashMap<>();
		definition = new HashMap<>();
	}

	public void markUsed(X64PreservedRegister used, int line) {
		TreeSet<Integer> mapping = usages.getOrDefault(used, new TreeSet<>());
		mapping.add(line);
		usages.putIfAbsent(used, mapping);
	}

	public void markDefined(X64PreservedRegister defined, int line) {
		definition.put(defined, line);
	}
}
