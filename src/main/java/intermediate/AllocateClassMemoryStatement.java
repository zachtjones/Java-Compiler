package intermediate;

import helper.CompileException;
import helper.Types;
import main.JavaCompiler;
import x64.X64Context;
import x64.allocation.CallingConvention;
import x64.instructions.CallClassMethod;
import x64.instructions.MoveInstruction;
import x64.operands.Immediate;

import java.util.HashMap;

import static x64.allocation.CallingConvention.argumentRegister;
import static x64.allocation.CallingConvention.returnValueRegister;

public class AllocateClassMemoryStatement implements InterStatement {

	private final Types type;
	private final Register result;

	/**
	 * Represents an allocation of memory for a class instance.
	 * @param type The fully-qualified class name.
	 * @param result The register to hold a reference to the memory allocated.
	 */
	public AllocateClassMemoryStatement(Types type, Register result) {
		this.type = type;
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "allocateType " + result + " = " +  type + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs,
			HashMap<String, Types> locals, HashMap<String, Types> params, InterFunction func) {
		
		result.setType(type);
		regs.put(result, type);
	}

	@Override
	public void compile(X64Context context) throws CompileException {
		// malloc (size_of class' instance structure) -> result
		InterFile temp = JavaCompiler.parseAndCompile(type.getClassName("", -1), "", -1);
		int size = temp.getClassSize();
		context.addInstruction(
			new MoveInstruction(
				new Immediate(size),
				context.argumentRegister(1)
			)
		);

		context.addInstruction(
			new CallClassMethod(CallingConvention.libraryFunc("malloc"))
		);

		// move returned value
		context.addInstruction(
			new MoveInstruction(
				returnValueRegister(),
				result.toX64()
			)
		);

		// TODO result->function_table = Class_function_table used in virtual function calls
	}
}
