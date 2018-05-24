package intermediate;

public class LabelStatement implements InterStatement {
	String name;
	
	public LabelStatement(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name + ": ;";
	}
}
