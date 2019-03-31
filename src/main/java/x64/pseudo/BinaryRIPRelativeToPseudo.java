package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegistersUsed;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;

public abstract class BinaryRIPRelativeToPseudo implements PseudoInstruction {

    @NotNull
	public final RIPRelativeData source;
    @NotNull
    public final X64PseudoRegister destination;
    private final String name;


    public BinaryRIPRelativeToPseudo(String name, @NotNull RIPRelativeData source,
									 @NotNull X64PseudoRegister destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markDefined(destination, i);
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String toString() {
        return '\t' + name + " " +
                source.toString() + ", " + destination.toString();
    }
}
