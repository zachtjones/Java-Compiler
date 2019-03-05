package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.List;
import java.util.Map;

/** Represents a conversion (truncation) of source -> destination. */
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

		//CVTSS2SI—Convert Scalar Single-Precision Floating-Point to Signed Double word or Quad word Integer
		//CVTSD2SI—Convert Scalar Double-Precision Floating-Point to Signed Double word or Quad word Integer
		//CVTTSS2SI—Convert Scalar Single-Precision Floating-Point to Signed Double word or Quad word Integer, Truncated
		//CVTTSD2SI—Convert Scalar Double-Precision Floating-Point to Signed Double word or Quad word Integer, Truncated

		if (source.isFloatingPoint()) {
			if (destination.isFloatingPoint()) {
				// double -> float(single)

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
	}
}
