package x64;

import intermediate.Register;
import intermediate.RegisterAllocator;
import x64.instructions.MoveInstruction;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.HashMap;
import java.util.Map;

import static x64.allocation.CallingConvention.argumentRegister;
import static x64.operands.X64RegisterOperand.of;

/**
 * This class serves the purpose of providing the adequate information for the
 * transition from intermediate language to x64.
 */
public class X64Context {

	private final X64File enclosingFile;
	private final X64Function function;
	private int nextRegister;
	private final X64RegisterOperand jniPointer;

	private final Map<Register, Register> instanceFieldAddressInstances = new HashMap<>();
	private final Map<Register, String> instanceFieldAddressNames = new HashMap<>();
	private final Map<Register, String> staticFieldAddressClasses = new HashMap<>();
	private final Map<Register, String> staticFieldAddressFields = new HashMap<>();
	private final Map<String, X64RegisterOperand> locals = new HashMap<>();

	public X64Context(X64File enclosingFile, RegisterAllocator allocator, String functionName) {

		this.enclosingFile = enclosingFile;
		this.nextRegister = allocator.getNextLabel();
		this.jniPointer = getNextQuadRegister();
		this.function = new X64Function(enclosingFile.getJavaName(), functionName, jniPointer);
	}

	/** Returns the next pseudo register available that is a quad-word size (64-bit) */
	public X64RegisterOperand getNextQuadRegister() {
		nextRegister++;
		return of(new X64PreservedRegister(nextRegister, Instruction.Size.QUAD));
	}

	/** Loads the JNI pointer into the first argument */
	public void loadJNI1() {
		function.addInstruction(new MoveInstruction(jniPointer, argumentRegister(1)));
	}

	/** Returns the register operand that holds the JNI pointer */
	public X64RegisterOperand getJniPointer() {
		return jniPointer;
	}

	/** Adds the function compiled to the enclosing file. */
	public void addFunctionToFile() {
		this.enclosingFile.addFunction(this.function);
	}

	/** Adds the instruction into the function being created. */
	public void addInstruction(Instruction instruction) {
		this.function.addInstruction(instruction);
	}

	/** Inserts a data string into the file, returning the label. */
	public String insertDataString(String content) {
		return enclosingFile.insertDataString(content);
	}

	// state-saving code for knowing what to do with the store at for instance fields
	public void markRegisterAsInstanceFieldAddress(Register result, Register instance, String fieldName) {
		instanceFieldAddressInstances.put(result, instance);
		instanceFieldAddressNames.put(result, fieldName);
	}

	public boolean registerIsInstanceFieldAddress(Register address) {
		return instanceFieldAddressInstances.containsKey(address);
	}

	public String getInstanceFieldAddressName(Register address) {
		return instanceFieldAddressNames.get(address);
	}

	public Register getInstanceFieldInstance(Register address) {
		return instanceFieldAddressInstances.get(address);
	}

	// state saving code for knowing what to do with the store at for static fields

	public void markRegisterAsStaticFieldAddress(Register result, String className, String fieldName) {
		staticFieldAddressClasses.put(result, className);
		staticFieldAddressFields.put(result, fieldName);
	}

	public boolean registerIsStaticFieldAddress(Register address) {
		return staticFieldAddressClasses.containsKey(address);
	}

	public String getStaticFieldAddressClassName(Register address) {
		return staticFieldAddressClasses.get(address);
	}

	public String getStaticFieldAddressFieldName(Register address) {
		return staticFieldAddressFields.get(address);
	}

	// state saving code for knowing what to do with the get/set local variables
	public void markRegisterAsLocalVariable(String name, X64RegisterOperand allocated) {
		locals.put(name, allocated);
	}

	public X64RegisterOperand getLocalVariable(String name) {
		return locals.get(name);
	}
}
