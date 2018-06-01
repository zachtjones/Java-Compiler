package intermediate;

/** store %src at %addr */
public class StoreAddressStatement implements InterStatement {
	Register src;
	Register addr;
	
	public StoreAddressStatement(Register src, Register addr) {
		this.src = src;
		this.addr = addr;
	}
	
	@Override
	public String toString() {
		return "store " + src.toString() + " at " + addr.toString() + ";";
	}
}
