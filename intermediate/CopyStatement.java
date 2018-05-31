package intermediate;

public class CopyStatement implements InterStatement {
	Register src;
	Register dest;
	
	public CopyStatement(Register src, Register dest) {
		this.src = src;
		this.dest = dest;
	}
	
	@Override
	public String toString() {
		return "copy " + dest.toString() + " = " + src.toString() + ";";
	}
}
