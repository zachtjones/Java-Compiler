package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.LoadEffectiveAddressRIPRelativeToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.operands.BasePointerOffset;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LoadEffectiveAddressRIPPseudo extends BinaryRIPRelativeToPseudo {

    public LoadEffectiveAddressRIPPseudo(@NotNull RIPRelativeData source, @NotNull X64PseudoRegister destination) {
        super("leaq", source, destination);
    }

    @Override
    public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
                                                        @NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
                                                        @NotNull X64Register temporaryImmediate) {

        if (mapping.containsKey(destination)) {
            return Collections.singletonList(
                new LoadEffectiveAddressRIPRelativeToReg(
                    source,
                    mapping.get(destination),
                    destination.getSuffix()
                )
            );
        } else {
            // can't do direct swap, need the temporary
            return Arrays.asList(
                new LoadEffectiveAddressRIPRelativeToReg(
                    source,
                    temporaryImmediate,
                    destination.getSuffix()
                ),
                new MoveRegToBasePointerOffset(
                    temporaryImmediate,
                    locals.get(destination),
                    destination.getSuffix()
                )
            );
        }
    }
}
