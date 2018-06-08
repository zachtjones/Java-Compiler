package intermediate;

public class CreateArrayStatement implements InterStatement {

	Register size;
	String type;
	Register result;

	public CreateArrayStatement(Register size, String type, Register result) {
		this.size = size;
		this.type = type;
		this.result = result;
	}

}
