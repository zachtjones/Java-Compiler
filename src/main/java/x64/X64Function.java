package x64;

import x64.directives.ByteAlignment;
import x64.directives.GlobalSymbol;
import x64.directives.LabelInstruction;
import x64.directives.SegmentChange;
import x64.instructions.MoveInstruction;
import x64.instructions.ReturnInstruction;
import x64.operands.X64NativeRegister;
import x64.operands.X64Register;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class X64Function {

	private final ArrayList<Instruction> prologue = new ArrayList<>();
	private final ArrayList<Instruction> contents = new ArrayList<>();
	private final ArrayList<Instruction> epilogue = new ArrayList<>();

	private int nextFreeRegister;

	public final X64Register javaEnvPointer;

	public X64Function(String javaClass, String javaMethod, int nextFreeRegister) {

		prologue.add(new SegmentChange(SegmentChange.TEXT));
		final String symbolName = SymbolNames.getFieldName(javaClass, javaMethod);
		prologue.add(new GlobalSymbol(symbolName));
		prologue.add(new ByteAlignment(16));
		prologue.add(new LabelInstruction(symbolName));

		javaEnvPointer = new X64Register(getNextFreeRegister(), X64Register.TEMPORARY, Instruction.Size.QUAD);

		// save the first argument, the java environment pointer to a dedicated virtual register.
		prologue.add(new MoveInstruction(X64NativeRegister.RDI, javaEnvPointer));

		epilogue.add(new ReturnInstruction());

		this.nextFreeRegister = nextFreeRegister;

	}

	/** Adds an instruction to this function */
	public void addInstruction(Instruction instruction) {
		this.contents.add(instruction);
	}

	/** Loads the JNI pointer into the first argument */
	public void loadJNI1() {
		X64Register firstArg = new X64Register(1, X64Register.ARGUMENT, Instruction.Size.QUAD);
		addInstruction(new MoveInstruction(javaEnvPointer, firstArg));
	}

	/** Gets the number associated with the next free register.
	 * (In this abstraction, there are an unlimited number) */
	public int getNextFreeRegister() {
		nextFreeRegister++;
		return nextFreeRegister;
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
