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
}
