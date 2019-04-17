package x64;

import helper.Types;
import intermediate.Register;
import intermediate.RegisterAllocator;
import org.jetbrains.annotations.NotNull;
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

	public static class Pair<X, Y> {
		public final X first;
		public final Y second;

		Pair(X first, Y second) {
			this.first = first;
			this.second = second;
		}
	}

	private final X64File enclosingFile;
	private final X64Function function;
	private int nextRegister;
	private final X64PseudoRegister jniPointer;

	private final Map<Register, Register> instanceFieldAddressInstances = new HashMap<>();
	private final Map<Register, String> instanceFieldAddressNames = new HashMap<>();
	private final Map<Register, String> staticFieldAddressClasses = new HashMap<>();
	private final Map<Register, String> staticFieldAddressFields = new HashMap<>();
	private final Map<String, X64PseudoRegister> locals = new HashMap<>();
	private final Map<Register, String> localsAddresses = new HashMap<>(); // address -> localName

	/** mapping of address -> (array, index) */
	private final Map<Register, Pair<Register, Register>> arrayValueAddresses = new HashMap<>();

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
	private X64PseudoRegister getNextRegister(X64InstructionSize size) {
		nextRegister++;
		return new X64PseudoRegister(nextRegister, size);
	}

	/** Returns the next pseudo register available for the given type. */
	public X64PseudoRegister getNextRegister(Types type) {
		return getNextRegister(type.x64Type());
	}

	/** Returns the next intermediate language register. This is used when you need a register to be
	 * operated on at the intermediate level code. */
	public Register getNextILRegister(Types type) {
		nextRegister++;
		return new Register(nextRegister, type, "auto-generated", -1);
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

	/** Deletes the local variable */
	public void clearLocalVariable(String name) {
		X64PseudoRegister reg = locals.get(name);
		function.markRegisterNeededToNow(reg);
		locals.remove(name);
	}

	/** returns the local variable's register that holds it's value (used for get/set)
	 * There isn't a need for an is local variable, since intermediate language is already type checked. */
	public X64PseudoRegister getLocalVariable(String name) {
		return locals.get(name);
	}

	/** marks the register as holding the address to the local variable */
	public void markRegisterAsLocalVariableAddress(String localName, Register destination) {
		localsAddresses.put(destination, localName);
	}

	/** returns if the register holds an local register address */
	public boolean registerIsLocalRegisterAddress(Register address) {
		return localsAddresses.containsKey(address);
	}

	/** Returns the mapping to the local name that the address means */
	public String getLocalAddressLocalName(Register address) {
		return localsAddresses.get(address);
	}

	/** Marks the register as holding the array and index address. */
	public void markRegisterAsArrayValueAddress(@NotNull Register address, @NotNull Register array,
												@NotNull Register index) {
		arrayValueAddresses.put(address, new Pair<>(array, index));
	}

	/** Returns if the register is an array value address. */
	public boolean registerIsArrayValueAddress(@NotNull Register address) {
		return arrayValueAddresses.containsKey(address);
	}

	public Pair<Register, Register> getRegisterArrayAndIndex(@NotNull Register address) {
		return arrayValueAddresses.get(address);
	}
}
