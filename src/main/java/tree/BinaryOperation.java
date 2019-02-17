package tree;

import org.jetbrains.annotations.NotNull;
import x64.pseudoInstruction.PseudoInstruction;
import x64.instructions.AddInstruction;
import x64.instructions.SubtractImmRegInstruction;
import x64.operands.DestinationOperand;
import x64.operands.SourceOperand;

/** These are the non short-circuiting binary operations (+ - / * % ^ & &lt;&lt; &gt;&gt; &gt;&gt;&gt;) */
public enum BinaryOperation {
	ADD("+"), SUBTRACT("-"),
	TIMES("*"), DIVIDE("/"), MOD("%"),
	AND("&"), XOR("^"), OR("|"),
	LEFT_SHIFT("<<"), RIGHT_SHIFT_SIGN(">>"), RIGHT_SHIFT_UNSIGNED(">>>"),
	CONCAT("+");

	private final String rep;
	BinaryOperation(String rep) {
		this.rep = rep;
	}

	/** returns the java representation of this binary operation */
	@NotNull public String getRepresentation() {
		return rep;
	}

	/***
	 * Returns the x64 instruction that is formed by this operation on the source and destination.
	 * Executes destination = destination op source.
	 * @param source The operand that is used as the source.
	 * @param destination The operation that is used as the destination.
	 * @return The instruction created.
	 */
	@NotNull public PseudoInstruction getInstruction(SourceOperand source, DestinationOperand destination) {
		switch (this){
			case ADD:
				return new AddInstruction(source, destination);
			case SUBTRACT:
				return new SubtractImmRegInstruction(source, destination);
			case TIMES:
				break;
			case DIVIDE:
				break;
			case MOD:
				break;
			case AND:
				break;
			case XOR:
				break;
			case OR:
				break;
			case LEFT_SHIFT:
				break;
			case RIGHT_SHIFT_SIGN:
				break;
			case RIGHT_SHIFT_UNSIGNED:
				break;
			case CONCAT:
				break;
		}
		throw new RuntimeException("Binary operation in helper not defined for non add-subtract.");
	}
}
