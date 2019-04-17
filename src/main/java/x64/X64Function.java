package x64;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import x64.allocation.RegisterTransformer;
import x64.directives.*;
import x64.instructions.*;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MoveRegToPseudo;
import x64.pseudo.PseudoInstruction;

import java.util.*;
import java.util.stream.Collectors;

import static x64.X64InstructionSize.QUAD;
import static x64.allocation.CallingConvention.*;
import static x64.operands.X64Register.RSP;

public class X64Function {

	private final ArrayList<PseudoInstruction> header = new ArrayList<>();
	private ArrayList<PseudoInstruction> contents = new ArrayList<>();

	private final X64Context context;

	@Nullable private RegisterTransformer.AllocationUnit au = null;

	@NotNull private HashMap<X64PseudoRegister, Integer> lastUsages = new HashMap<>();

	X64Function(String javaClass, String javaMethod, X64PseudoRegister jniEnvPointer, X64Context context) {
		this.context = context;

		header.add(new SegmentChange(SegmentChange.TEXT));
		final String symbolName = SymbolNames.getFieldName(javaClass, javaMethod);
		header.add(new GlobalSymbol(symbolName));
		// linux define
		// .type Symbol_Name, @function
		if (!isMicrosoft && !isMac) {
			header.add(new TypeDirective(symbolName));
		}
		header.add(new ByteAlignment(16));
		header.add(new LabelInstruction(symbolName));

		// save the first argument, the java environment pointer to a dedicated virtual register.
		contents.add(new MoveRegToPseudo(argumentRegister(1), jniEnvPointer));
	}

	/** Adds an instruction to this function */
	public void addInstruction(PseudoInstruction instruction) {
		this.contents.add(instruction);
	}

	/** Marks the register as needed until the now.
	 * This is used for local variables needing a spot until their end of scope. */
	void markRegisterNeededToNow(X64PseudoRegister reg) {
		lastUsages.put(reg, contents.size());
	}

	/** Allocates the registers, transforming pseudo-registers to real ones */
	void allocateRegisters() {
		au = new RegisterTransformer(contents, context, lastUsages).allocate();
	}

	@Override
	public String toString() {

		final List<PseudoInstruction> allInstructions = new ArrayList<>(header);

		if (au != null) allInstructions.addAll(au.prologue);

		if (needsToAllocate32BytesForArgs())
			allInstructions.add(new SubtractImmToReg(new Immediate(32), RSP, QUAD));

		if (au == null) {
			allInstructions.addAll(contents);
		} else {
			allInstructions.addAll(au.instructions);
		}

		if (needsToAllocate32BytesForArgs())
			allInstructions.add(new AddImmReg(new Immediate(32), RSP, QUAD));

		if (au != null) allInstructions.addAll(au.epilogue);

		allInstructions.add(Return.instance);

		return allInstructions.stream()
				.map(PseudoInstruction::toString)
				.collect(Collectors.joining("\n"));
	}
}
