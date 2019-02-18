package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.instructions.CallRegDisplacement;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.operands.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Represents a call to a register relative memory pointer, callq *16(%q10) for example */
public class CallPseudoDisplacement implements PseudoInstruction {

	@NotNull private final PseudoDisplacement temp;

	public CallPseudoDisplacement(@NotNull PseudoDisplacement temp) {
		this.temp = temp;
	}

	@Override
	public boolean isCalling() {
		return true;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(temp.register, i);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
														@NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
														@NotNull X64NativeRegister temporaryImmediate) {
		if (mapping.containsKey(temp.register)) {
			// simple mapping
			return Collections.singletonList(
				new CallRegDisplacement(new RegDisplacement(temp.offset, mapping.get(temp.register)))
			);
		} else {
			return Arrays.asList(
				// can't to a displacement of a displacement of the base pointer
				new MoveBasePointerOffsetToReg(locals.get(temp.register), temporaryImmediate),
				new CallRegDisplacement(new RegDisplacement(temp.offset, temporaryImmediate))
			);
		}
	}

	@Override
	public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
		mapping.get(temp.register).increment();
	}

	@Override
	public String toString() {
		return "\tcall *" + temp.toString();
	}
}
