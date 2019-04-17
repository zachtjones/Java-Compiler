package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.*;
import x64.operands.PseudoAbsolute;
import x64.operands.RegAbsolute;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/***
 * Represents a binary instruction that involves a memory at pseudo register source and pseudo register destination.
 */
public abstract class BinaryPseudoAbsoluteToPseudo implements PseudoInstruction {
	@NotNull public final PseudoAbsolute source;
	@NotNull public final X64PseudoRegister destination;
	private final String name;


	BinaryPseudoAbsoluteToPseudo(String name, @NotNull PseudoAbsolute source,
										@NotNull X64PseudoRegister destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markDefined(destination, i);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		if (context.isRegister(source.source)) {
			if (context.isRegister(destination)) {
				return Collections.singletonList(
					createThisRegAbsoluteToReg(
						new RegAbsolute(context.getRegister(source.source)),
						context.getRegister(destination)
					)
				);
			} else {
				return Arrays.asList(
					createThisRegAbsoluteToReg(
						new RegAbsolute(context.getRegister(source.source)),
						context.getScratchRegister()
					),
					new MoveRegToBPOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		} else {
			if (context.isRegister(destination)) {
				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(source.source),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					createThisRegAbsoluteToReg(
						new RegAbsolute(context.getScratchRegister()),
						context.getRegister(destination)
					)
				);
			} else {
				// both are mapped to memory, but the first is already mapped to memory
				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(source.source),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					createThisRegAbsoluteToReg(
						new RegAbsolute(context.getScratchRegister()),
						context.getScratchRegister()
					),
					new MoveRegToBPOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		}
	}

	abstract BinaryAbsoluteRegToReg createThisRegAbsoluteToReg(@NotNull RegAbsolute source,
															   @NotNull X64Register destination);


	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			source.toString() + ", " + destination.toString();
	}
}
