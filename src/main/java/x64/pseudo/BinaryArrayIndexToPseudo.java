package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.NotSecondScratchException;
import x64.allocation.RegistersUsed;
import x64.instructions.BinaryArrayIndexToReg;
import x64.instructions.Instruction;
import x64.operands.PseudoIndexing;
import x64.operands.RegIndexing;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.List;

public abstract class BinaryArrayIndexToPseudo implements PseudoInstruction {
	@NotNull private final PseudoIndexing source;
	@NotNull
	final X64PseudoRegister destination;

	public BinaryArrayIndexToPseudo(@NotNull PseudoIndexing source, @NotNull X64PseudoRegister destination) {
		this.source = source;
		this.destination = destination;
	}

	/**
	 * Creates the subclass's binary op source, offset(base, index, scaling)
	 * @param source The register allocated to the source.
	 * @param destination The register indexing destination.
	 * @return The subclass.
	 */
	abstract BinaryArrayIndexToReg
		createThisArrayIndexToReg(@NotNull RegIndexing source, @NotNull X64Register destination);

	@Override
	public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context)
		throws NotSecondScratchException {

		// binary op source, offset(base, index, scaling)

		throw new RuntimeException("Binary ArrayIndex -> Pseudo operation not implemented for allocation.");
	}

	@Override
	public final void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(source.base, i);
		usedRegs.markUsed(source.index, i);
		// defines the result
		usedRegs.markDefined(destination, i);
	}
}
