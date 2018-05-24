package intermediate;

/** Represents the ending of a scope of a local variable. */
public class EndScopeStatement implements InterStatement {
	String name;
	
	/**
	 * Constructs a local variable scope ending statement.
	 * @param name The name of the local variable.
	 */
	public EndScopeStatement(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "EndScope " + name + ";";
	}
}
