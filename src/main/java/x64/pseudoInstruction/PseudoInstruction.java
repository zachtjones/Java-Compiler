package x64.pseudoInstruction;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.List;
import java.util.Map;

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
     * @param mapping A mapping of the pseudo register to a native one for those that can be.
     * @param locals A mapping of the pseudo register to a base pointer offset for the other ones.
     * @param temporaryImmediate A temporary register used when an instruction would use 2 memory operands.
     * @return A list of the instructions that result in the allocation.
     * This will be a list of 1+ elements, usually 2 if there's 2 memory operands, but could be more for edge cases.
     */
    @NotNull List<@NotNull PseudoInstruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
                                                       @NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
                                                       @NotNull X64NativeRegister temporaryImmediate);

    /** Increments the priority of the allocated register when it is used.
     * If no pseudo registers are used, don't have to implement this method.
     * @param mapping The RegisterMapped instance that each pseudo register is mapped to */
    default void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {}

    /** Represents how this instruction should be represented in x64 assembly */
    String toString();
}
