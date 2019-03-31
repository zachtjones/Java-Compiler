package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.instructions.CallRegDisplacement;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.operands.PseudoDisplacement;
import x64.operands.RegDisplacement;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static x64.X64InstructionSize.QUAD;

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
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		if (context.isRegister(temp.register)) {
			// simple mapping
			return Collections.singletonList(
				new CallRegDisplacement(new RegDisplacement(temp.offset, context.getRegister(temp.register)))
			);
		} else {
			return Arrays.asList(
				// can't to a displacement of a displacement of the base pointer
				//  also we know it's going to be a quad word since address
				new MoveBasePointerOffsetToReg(context.getBasePointer(temp.register), context.getScratchRegister(), QUAD),
				new CallRegDisplacement(new RegDisplacement(temp.offset, context.getScratchRegister()))
			);
		}
	}

	@Override
	public void prioritizeRegisters(Map<X64PseudoRegister, RegisterMapped> mapping) {
		context.getRegister(temp.register).increment();
	}

	@Override
	public String toString() {
		return "\tcall *" + temp.toString();
	}
}
