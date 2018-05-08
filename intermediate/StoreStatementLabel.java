package intermediate;

/**
 * store REGISTER to LABEL
 * @author zach jones
 */
public class StoreStatementLabel implements InterStatement {
	int registerType;
	int registerNumber;
	String label;

	/**
	 * Creates a store statement, with the destination as a label.
	 * @param registerType The register type to store
	 * @param registerNumber The register number to store.
	 * @param label The label that is the address.
	 */
	public StoreStatementLabel(int registerType, int registerNumber, String label) {
		this.registerType = registerType;
		this.registerNumber = registerNumber;
		this.label = label;
	}

	public String toString() {
		return "store " + RegisterType.toString(registerType) + 
				registerNumber + " at " + label + ";";
	}
}
