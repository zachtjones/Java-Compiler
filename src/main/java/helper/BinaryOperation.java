package helper;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64PseudoRegister;
import x64.pseudo.AddPseudoToPseudo;
import x64.pseudo.PseudoInstruction;
import x64.pseudo.SignedMultiplyPseudoToPseudo;
import x64.pseudo.SubtractPseudoToPseudo;

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
	 * Returns the instruction that is responsible for source = source OP destination
	 * @param source The source part, represented as a pseudo register
	 * @param destination The destination pseudo register.
	 * @return An pseudo instruction representing that computation.
	 */
	public PseudoInstruction getInstruction(@NotNull X64PseudoRegister source,
											@NotNull X64PseudoRegister destination) {
		switch (this){
			case ADD:
				return new AddPseudoToPseudo(source, destination);

			case SUBTRACT:
				return new SubtractPseudoToPseudo(source, destination);

			case TIMES:
				return new SignedMultiplyPseudoToPseudo(source, destination);

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
		throw new RuntimeException("Only +/- binary ops implemented in BinaryOperation.java Enum class so far.");
	}
}
