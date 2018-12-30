package x64;

import x64.allocation.RegisterTransformer;
import x64.directives.ByteAlignment;
import x64.directives.GlobalSymbol;
import x64.directives.LabelInstruction;
import x64.directives.SegmentChange;
import x64.instructions.MoveInstruction;
import x64.instructions.PopInstruction;
import x64.instructions.PushInstruction;
import x64.instructions.ReturnInstruction;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static x64.operands.X64RegisterOperand.of;

public class X64Function {

	private final ArrayList<Instruction> prologue = new ArrayList<>();
	private ArrayList<Instruction> contents = new ArrayList<>();
	private final ArrayList<Instruction> epilogue = new ArrayList<>();

	private int nextFreeRegister;

	public final X64RegisterOperand javaEnvPointer;

	public X64Function(String javaClass, String javaMethod, int nextFreeRegister) {

		prologue.add(new SegmentChange(SegmentChange.TEXT));
		final String symbolName = SymbolNames.getFieldName(javaClass, javaMethod);
		prologue.add(new GlobalSymbol(symbolName));
		prologue.add(new ByteAlignment(16));
		prologue.add(new LabelInstruction(symbolName));

		this.nextFreeRegister = nextFreeRegister;
		
		javaEnvPointer = of(new X64PreservedRegister(getNextFreeRegister(), Instruction.Size.QUAD));

		// save the first argument, the java environment pointer to a dedicated virtual register.
		contents.add(new MoveInstruction(X64NativeRegister.RDI, javaEnvPointer));

		epilogue.add(new ReturnInstruction());
	}

	/** Adds an instruction to this function */
	public void addInstruction(Instruction instruction) {
		this.contents.add(instruction);
	}

	/** Loads the JNI pointer into the first argument */
	public void loadJNI1() {
		addInstruction(new MoveInstruction(javaEnvPointer, X64NativeRegister.RDI));
	}

	/** Gets the number associated with the next free register.
	 * (In this abstraction, there are an unlimited number) */
	public int getNextFreeRegister() {
		nextFreeRegister++;
		return nextFreeRegister;
	}

	/** Allocates the registers, transforming pseudo-registers to real ones */
	void allocateRegisters() {
		Set<X64NativeRegister> usedRegs = new RegisterTransformer(contents).allocate();

		// build up the push @ beginning / pop before return for used registers
		for (X64NativeRegister usedReg : usedRegs) {
			this.prologue.add(
				new PushInstruction(of(usedReg))
			);
			this.epilogue.add(0,
				new PopInstruction(of(usedReg))
			);
		}
	}

	@Override
	public String toString() {

		final List<Instruction> allInstructions = new ArrayList<>();
		allInstructions.addAll(prologue);
		allInstructions.addAll(contents);
		allInstructions.addAll(epilogue);

		return allInstructions.stream()
				.map(Instruction::toString)
				.collect(Collectors.joining("\n"));
	}
}
