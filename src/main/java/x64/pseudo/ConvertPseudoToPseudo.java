package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.Instruction;
import x64.operands.X64PseudoRegister;

import java.util.List;

/** Represents a conversion (truncation) of source -> destination.
 * Note that is the conversion is not to a smaller type, then allocation to real regs will be undefined. */
public class ConvertPseudoToPseudo implements PseudoInstruction {
	@NotNull private final X64PseudoRegister source;
	@NotNull private final X64PseudoRegister destination;

	public ConvertPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		// can't extend binary pseudo to pseudo
		this.source = source;
		this.destination = destination;
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		// instruction notes:
		//  CVTSS2SD -- scalar single -> scalar double
		//  cvtsd2ss -- scalar double -> scalar single
		//  binary ops with floating point will be done with scalars (single and doubles), not packed ones
		//    ex: adds(s|d)

		// all these use the xmm registers, or ymm if processor has those
		//  xmm has 128 bits with packed data, but we're going to use scalars
		//  we can add optimizations later to do loop unrolling to get the full width of these

		//CVTSS2SI—Convert Scalar Single-Precision Floating-Point to Signed d word or q word Integer
		//CVTSD2SI—Convert Scalar Double-Precision Floating-Point to Signed d word or q word Integer
		//CVTTSS2SI—Convert Scalar Single-Precision Floating-Point to Signed d word or q word Integer, Truncated
		//CVTTSD2SI—Convert Scalar Double-Precision Floating-Point to Signed d word or q word Integer, Truncated

		// also have the 4 choices for source and destination allocation.

		// TODO create the instructions
		if (source.isFloatingPoint() || destination.isFloatingPoint()) {
			throw new RuntimeException("Floating point conversions not implemented yet.");
		}

		// truncation of integral types -- simple read as smaller type in a later instruction
		X64PseudoRegister newDest = new X64PseudoRegister(destination.getNumber(), source.getSuffix());
		return new MovePseudoToPseudo(source, newDest).allocate(context);
	}

	@Override
	public String toString() {
		return "cvt " + source + ", " + destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(source, i);
		usedRegs.markDefined(destination, i);
	}
}
