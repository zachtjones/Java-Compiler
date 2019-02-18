package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.operands.MemoryAtPseudo;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

public class MovePseudoAbsoluteToPseudo extends BinaryPseudoAbsoluteToPseudo {

	public MovePseudoAbsoluteToPseudo(@NotNull MemoryAtPseudo source,
									  @NotNull X64PreservedRegister destination) {
		super("mov", source, destination);
	}
}
