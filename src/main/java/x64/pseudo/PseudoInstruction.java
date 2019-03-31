package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.Instruction;

import java.util.List;

/** These are instructions that deal with pseudo registers, instead of the real ones. */
public interface PseudoInstruction {

    /** Returns true if and only if this instruction is a variety of call */
    default boolean isCalling() {
        return false;
    }

    /** Call the methods for the marking the registers as used / defined. */
    default void markRegisters(int i, RegistersUsed usedRegs) {}

    /**
     * Allocates the pseudo registers in these instructions to real ones / base pointer offsets.
     * The pseudo registers will be in exactly one of the maps.
     * @param context The context that holds which registers are mapped to which ones.
     * @return A list of the instructions that result in the allocation.
     * This will be a list of 1+ elements, usually 2 if there's 2 memory operands, but could be more for edge cases.
     */
    @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context);

    /** Represents how this instruction should be represented in x64 assembly */
    String toString();
}
