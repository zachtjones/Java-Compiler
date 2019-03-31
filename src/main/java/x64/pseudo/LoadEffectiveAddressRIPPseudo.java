package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.LoadEffectiveAddressRIPRelativeToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoadEffectiveAddressRIPPseudo extends BinaryRIPRelativeToPseudo {

    public LoadEffectiveAddressRIPPseudo(@NotNull RIPRelativeData source, @NotNull X64PseudoRegister destination) {
        super("leaq", source, destination);
    }

    @Override
    public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

        if (context.isRegister(destination)) {
            return Collections.singletonList(
                new LoadEffectiveAddressRIPRelativeToReg(
                    source,
                    context.getRegister(destination),
                    destination.getSuffix()
                )
            );
        } else {
            // can't do direct swap, need the temporary
            return Arrays.asList(
                new LoadEffectiveAddressRIPRelativeToReg(
                    source,
                    context.getScratchRegister(),
                    destination.getSuffix()
                ),
                new MoveRegToBasePointerOffset(
                    context.getScratchRegister(),
                    context.getBasePointer(destination),
                    destination.getSuffix()
                )
            );
        }
    }
}
