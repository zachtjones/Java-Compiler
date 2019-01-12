package x64;

import x64.allocation.RegisterTransformer;
import x64.directives.ByteAlignment;
import x64.directives.GlobalSymbol;
import x64.directives.LabelInstruction;
import x64.directives.SegmentChange;
import x64.instructions.*;
import x64.allocation.X64NativeRegister;
import x64.operands.Immediate;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static x64.allocation.CallingConvention.argumentRegister;
import static x64.allocation.CallingConvention.needsToAllocate32BytesForArgs;
import static x64.allocation.X64NativeRegister.RSP;
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
		
		javaEnvPointer = this.getNextQuadRegister();

		// save the first argument, the java environment pointer to a dedicated virtual register.
		contents.add(new MoveInstruction(argumentRegister(1), javaEnvPointer));

		epilogue.add(new ReturnInstruction());
	}

	/** Adds an instruction to this function */
	public void addInstruction(Instruction instruction) {
		this.contents.add(instruction);
	}

	/** Loads the JNI pointer into the first argument */
	public void loadJNI1() {
		addInstruction(new MoveInstruction(javaEnvPointer, argumentRegister(1)));
	}

	/** Returns the next pseudo register available that is a quad-word size (64-bit) */
	public X64RegisterOperand getNextQuadRegister() {
		nextFreeRegister++;
		return of(new X64PreservedRegister(nextFreeRegister, Instruction.Size.QUAD));
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

		// do the allocation of 32 bytes stack space for callee to save arguments if needed by the calling convention
		if (needsToAllocate32BytesForArgs()) {
			prologue.add(new SubtractInstruction(
				new Immediate(32),
				RSP
			));

			epilogue.add(0, new AddInstruction(
				new Immediate(32),
				RSP
			));
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
