package x64.pseudoInstruction;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.operands.*;

import java.util.List;
import java.util.Map;

/** Represents a call to a register relative memory pointer, callq *16(%q10) for example */
public class CallRegisterDisplacementPseudo implements PseudoInstruction {

	@NotNull private final RegisterRelativePointer temp;

	public CallRegisterDisplacementPseudo(@NotNull RegisterRelativePointer temp) {
		this.temp = temp;
	}

	@Override
	public boolean isCalling() {
		return true;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		temp.markUsed(i, usedRegs);
	}

	@Override
	public @NotNull List<@NotNull PseudoInstruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
															  @NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
															  @NotNull X64NativeRegister temporaryImmediate) {
		if (mapping.containsKey(temp.getRegister())) {
			// simple mapping

		}

		return null;
	}

	@Override
	public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
		temp.prioritizeRegisters(mapping);
	}

	@Override
	public String toString() {
		return "\tcall *" + temp.toString();
	}
}
