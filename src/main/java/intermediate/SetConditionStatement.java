package intermediate;

import conversions.Conversion;
import helper.CompileException;
import helper.ConditionCode;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.pseudo.ComparePseudoPseudo;
import x64.pseudo.SetConditionPseudo;

import java.util.HashMap;
import java.util.List;

import static helper.ConditionCode.EQUAL;
import static helper.ConditionCode.NOT_EQUAL;

public class SetConditionStatement implements InterStatement {
	
	@NotNull private final ConditionCode type;
	@NotNull private final Register left;
	@NotNull private final Register right;
	@NotNull private final Register result;
	
	@NotNull private final String fileName;
	private final int line;

	// conversion used in being able to compare the two types
	private List<InterStatement> conversionLeft;
	private List<InterStatement> conversionRight;
	private Register leftConverted;
	private Register rightConverted;


	/** Sets the boolean result to 1 when the condition holds, 0 otherwise. */
	public SetConditionStatement(@NotNull ConditionCode type, @NotNull Register left, @NotNull Register right,
								 @NotNull Register result,
								 @NotNull String fileName, int line) {
		this.type = type;
		this.left = left;
		this.right = right;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "setCondition " + result + " = " + left + " " + type + " " + right + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		if (type != NOT_EQUAL && type != EQUAL) { // == and != can be used with objects
			// type is a relational only defined on primitives
			if (!left.isPrimitive() || !right.isPrimitive()) {
				throw new CompileException("Relational operators other than == and"
						+ " != are only defined on primitives.", fileName, line);
			}
		}

		Types larger = left.getType().getResult(right.getType());

		// add the conversions necessary
		leftConverted = func.allocator.getNext(larger);
		rightConverted = func.allocator.getNext(larger);
		conversionLeft = Conversion.assignmentConversion(left, leftConverted, fileName, line);
		conversionRight = Conversion.assignmentConversion(right, rightConverted, fileName, line);

		result.setType(Types.BOOLEAN);
		regs.put(result, Types.BOOLEAN);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		// compare the operands
		// SETcc, where cc is the condition code. Note this sets a byte register/mem
		//  the result byte will have the value 1 if the condition holds, 0 otherwise.

		// convert left
		for (InterStatement s : conversionLeft) {
			s.compile(context);
		}

		// convert right
		for (InterStatement s : conversionRight) {
			s.compile(context);
		}

		// compare the converted types
		context.addInstruction(
			new ComparePseudoPseudo(leftConverted.toX64(), rightConverted.toX64())
		);

		context.addInstruction(
			new SetConditionPseudo(type, result.toX64())
		);
	}
}
