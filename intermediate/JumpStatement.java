package intermediate;

/** jump LABEL; */
public class JumpStatement implements InterStatement {
	LabelStatement label;
	
	public JumpStatement(LabelStatement label) {
		this.label = label;
	}
	
	public String toString() {
		return "jump " + label.name + ";";
	}
}
