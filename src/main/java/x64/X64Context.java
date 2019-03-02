package x64;

import intermediate.Register;
import intermediate.RegisterAllocator;
import x64.allocation.CallingConvention;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;
import x64.pseudo.MovePseudoToReg;
import x64.pseudo.PseudoInstruction;

import java.util.HashMap;
import java.util.Map;


/**
 * This class serves the purpose of providing the adequate information for the
 * transition from intermediate language to x64.
 */
public class X64Context {

	private final X64File enclosingFile;
	private final X64Function function;
	private int nextRegister;
	private final X64PseudoRegister jniPointer;

	private final Map<Register, Register> instanceFieldAddressInstances = new HashMap<>();
	private final Map<Register, String> instanceFieldAddressNames = new HashMap<>();
	private final Map<Register, String> staticFieldAddressClasses = new HashMap<>();
	private final Map<Register, String> staticFieldAddressFields = new HashMap<>();
	private final Map<String, X64PseudoRegister> locals = new HashMap<>();

	/** Returns the highest number of argument register used */
	private int highestArgUsed = 1; // reserve the JNI register as always used

	public X64Context(X64File enclosingFile, RegisterAllocator allocator, String functionName) {

		this.enclosingFile = enclosingFile;
		this.nextRegister = allocator.getNextLabel();
		this.jniPointer = getNextQuadRegister();
		this.function = new X64Function(enclosingFile.getJavaName(), functionName, jniPointer, this);
	}

	/** Wrapper for CallingConvention.argumentRegister, used to keep track of highest number used */
	public X64Register argumentRegister(int number) {
		highestArgUsed = Math.max(number, highestArgUsed);
		return CallingConvention.argumentRegister(number);
	}

	/** Returns the next pseudo register available that is a quad-word size (64-bit) */
	public X64PseudoRegister getNextQuadRegister() {
		return getNextRegister(X64InstructionSize.QUAD);
	}

	/** Returns the next pseudo register available for the instruction size instance. */
	X64PseudoRegister getNextRegister(X64InstructionSize size) {
		nextRegister++;
		return new X64PseudoRegister(nextRegister, size);
	}

	/** returns the argument number that is the highest number used */
	public int getHighestArgUsed() {
		return highestArgUsed;
	}

	/** Loads the JNI pointer into the first argument */
	public void loadJNI1() {
		function.addInstruction(new MovePseudoToReg(jniPointer, argumentRegister(1)));
	}

	/** Returns the register operand that holds the JNI pointer */
	public X64PseudoRegister getJniPointer() {
		return jniPointer;
	}

	/** Adds the function compiled to the enclosing file. */
	public void addFunctionToFile() {
		this.enclosingFile.addFunction(this.function);
	}

	/** Adds the instruction into the function being created. */
	public void addInstruction(PseudoInstruction instruction) {
		this.function.addInstruction(instruction);
	}

	/** Inserts a data string into the file, returning the label. */
	public String insertDataString(String content) {
		return enclosingFile.insertDataString(content);
	}

	/** Marks a register as holding an instance field address. */
	public void markRegisterAsInstanceFieldAddress(Register result, Register instance, String fieldName) {
		instanceFieldAddressInstances.put(result, instance);
		instanceFieldAddressNames.put(result, fieldName);
	}

	/** returns if the register holds an instance field address */
	public boolean registerIsInstanceFieldAddress(Register address) {
		return instanceFieldAddressInstances.containsKey(address);
	}

	/** returns the instance field address' field name. */
	public String getInstanceFieldAddressName(Register address) {
		return instanceFieldAddressNames.get(address);
	}

	/** returns the instance field address' register that holds the object */
	public Register getInstanceFieldInstance(Register address) {
		return instanceFieldAddressInstances.get(address);
	}

	/** Marks a register as a static field address. */
	public void markRegisterAsStaticFieldAddress(Register result, String className, String fieldName) {
		staticFieldAddressClasses.put(result, className);
		staticFieldAddressFields.put(result, fieldName);
	}

	/** returns true if the registers is marked as a static field address */
	public boolean registerIsStaticFieldAddress(Register address) {
		return staticFieldAddressClasses.containsKey(address);
	}

	/** returns the static field address' class name */
	public String getStaticFieldAddressClassName(Register address) {
		return staticFieldAddressClasses.get(address);
	}

	/** returns the static field address' field name */
	public String getStaticFieldAddressFieldName(Register address) {
		return staticFieldAddressFields.get(address);
	}

	/** Marks the register as a local variable */
	public void markRegisterAsLocalVariable(String name, X64PseudoRegister allocated) {
		locals.put(name, allocated);
	}

	/** returns the local variable's register that holds it's value (used for get/set)
	 * There isn't a need for an is local variable, since intermediate language is already type checked. */
	public X64PseudoRegister getLocalVariable(String name) {
		return locals.get(name);
	}
}
