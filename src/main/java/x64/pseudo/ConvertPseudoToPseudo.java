package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.List;
import java.util.Map;

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
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {
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

		if (source.isFloatingPoint()) {
			if (destination.isFloatingPoint()) {
				// double -> float(single)
//				return Collections.singletonList(
//					new ConvertScalarDoubleToScalarSingle(source, destination)
//				);
			} else {
				// double/single -> integral type

			}
		} else {
			if (destination.isFloatingPoint()) {
				// integral type -> floating point

			} else {
				// both are integers, just read source as a smaller type to destination
				// can use a movePseudoToPseudo of sourceConverted, destination
			}
		}
		throw new RuntimeException("Not implemented yet");
	}
}
