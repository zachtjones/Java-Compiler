package intermediate;

/** throw REGISTER */
public class ThrowStatement implements InterStatement {
	Register r;
	
	public ThrowStatement(Register r) {
		this.r = r;
	}
	
	@Override
	public String toString() {
		return "throw " + r.toString() + ";";
	}
}
