package intermediate;

import java.util.HashMap;

import helper.CompileException;

public class CreateArrayStatement implements InterStatement {

	Register size;
	String type;
	Register result;

	public CreateArrayStatement(Register size, String type, Register result) {
		this.size = size;
		this.type = type;
		this.result = result;
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs) throws CompileException {
		regs.put(result, type + "[]");
		
	}

}
