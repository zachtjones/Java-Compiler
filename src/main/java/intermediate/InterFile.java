package intermediate;

import java.util.*;

import helper.CompileException;
import helper.Types;
import tree.NameNode;
import x64.X64File;

import static helper.Types.fromFullyQualifiedClass;

public class InterFile {
	private String name;
	private InterStructure staticPart;
	private InterStructure instancePart;
	private ArrayList<InterFunction> functions;

	private ArrayList<NameNode> implementsNodes;
	private NameNode superNode;

	/** name -> Map of list of args to return value */
	private final Map<String, Map<List<Types>, Types>> typesOfFunctions;

	/**
	 * Creates an intermediate file, given the name
	 * @param name The name, such as java/util/Scanner
	 */
	public InterFile(String name) {
		this.name = name;
		this.staticPart = new InterStructure(false);
		this.instancePart = new InterStructure(true);
		this.functions = new ArrayList<>();
		typesOfFunctions = new HashMap<>();
	}

	/**
	 * Adds a field to the intermediate file
	 * @param type The string that is the type of the field
	 * @param name The identifier of the field
	 * @param isStatic true if the field is static, 
	 * false if it is instance-based.
	 */
	public void addField(Types type, String name, boolean isStatic) {
		addField(type, name, isStatic, null);
	}

	/**
	 * Adds a field to the intermediate file
	 * @param type The string that is the type of the field
	 * @param name The identifier of the field
	 * @param isStatic true if the field is static, 
	 * @param value The default value of the field.
	 * false if it is instance-based.
	 */
	public void addField(Types type, String name, boolean isStatic, String value) {
		if (isStatic) {
			this.staticPart.addMember(type, name, value);
		} else {
			this.instancePart.addMember(type, name, value);
		}
	}

	/** Gets the file's name (with package, using '/') as a String.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("// file: ");
		result.append(name);
		result.append(".jil\n\n");

		// super class
		if (this.superNode != null) {
			result.append("super ");
			result.append(this.superNode.primaryName);
			result.append('\n');
		}

		// interfaces
		if (this.implementsNodes != null) {
			result.append("implements ");
			for (NameNode n : this.implementsNodes) {
				result.append(n.primaryName);
				result.append(' ');
			}
			result.append('\n');
		}

		// instance structure
		result.append(instancePart.toString());

		// static structure
		result.append(staticPart.toString());

		// functions
		for (InterFunction f : functions) {
			result.append(f.toString());
		}

		return result.toString();
	}

	/**
	 * Sets the list of Names that this IL file implements
	 * @param names The list of NameNode's
	 */
	public void setImplements(ArrayList<NameNode> names) {
		this.implementsNodes = names;
	}

	/**
	 * Sets the name that this IL file has as a superclass
	 * @param superclass The NameNode representing the superclass.
	 */
	public void setExtends(NameNode superclass) {
		this.superNode = superclass;
	}

	/**
	 * Adds an intermediate file function to the list.
	 * @param func The IL function.
	 */
	public void addFunction(InterFunction func) {
		this.functions.add(func);
	}

	/**
	 * Generates the default constructor if needed.
	 */
	public void generateDefaultConstructor(String fileName, int line) {
		boolean hasOne = false;
		for (InterFunction i : functions) {
			if (i.name.equals("<init>")) {
				hasOne = true;
			}
		}
		if (!hasOne) {
			InterFunction d = new InterFunction(name);
			// add the name and return type
			d.name = "<init>";
			d.isInstance = true;
			d.returnType = Types.VOID;

			// TODO call to super (that's pretty complicated since the test classes extend object)
			// in order to call a superclass that is part of the java library, have to do some sort of
			//  re-arrangement of the field order

			// add the only statement, super();
			//CallActualStatement c;
			//Register[] args = new Register[0];

			// load this pointer (call super on this)
			//RegisterAllocator ra = new RegisterAllocator();
			//Register thisPointer = ra.getNext(fromFullyQualifiedClass(this.name));
			//d.statements.add(new GetParamStatement(thisPointer, "this", fileName, line));
			//Register voidReg = ra.getNext(Types.VOID);
			//  superclass of this object.
			// add in the call to it's init
			//CallActualStatement c = new CallActualStatement(thisPointer, superNode.primaryName, "<init>",
			// args, voidReg, fileName, line);
			//d.statements.add(c);
			functions.add(d);
		}
	}

	/** Type checks all the functions 
	 * @throws CompileException If there is an error with type checking.*/
	public void typeCheck() throws CompileException {
		
		for (InterFunction f : functions) {
			f.typeCheck(fromFullyQualifiedClass(name));
			// add it to the set
			Map<List<Types>, Types> existing = typesOfFunctions.getOrDefault(f.name, new HashMap<>());
			existing.put(f.paramTypes, f.returnType);
			typesOfFunctions.putIfAbsent(f.name, existing);
		}

	}

	/**
	 * Gets the type of the field for this structure
	 * @param fieldName The field's name
	 * @return The JIL representation of the type
	 * @throws CompileException if the field doesn't exist, or there is a problem checking it.
	 */
	public Types getInstFieldType(String fieldName, String fileName, int line) throws CompileException {
		return instancePart.getFieldType(fieldName, fileName, line);
	}

	/**
	 * Gets the type of the field for this class static field.
	 * @param fieldName The field's name
	 * @return The JIL representation of the type
	 * @throws CompileException if the field doesn't exist, or there is a problem checking it.
	 */
	public Types getStatFieldType(String fieldName, String fileName, int line) throws CompileException {
		return staticPart.getFieldType(fieldName, fileName, line);
	}

	/**
	 * Finds the method with the signature given, and returns the type of the return object
	 * @param name The method's name.
	 * @param args The array of arguments.
	 * @return The return object type, or null if there's no method with that signature
	 */
	public Types getReturnType(String name, ArrayList<Types> args) {
		if (typesOfFunctions.containsKey(name)) {
			return typesOfFunctions.get(name).get(args);
		}
		return null;
	}

	/**
	 * Creates an X64 file representing this intermediate file.
	 * @return The X64 assembly file.
	 * @throws CompileException If there is a problem converting the IL to the assembly.
	 */
	public X64File compileX64() throws CompileException {

		// TODO use the inheritance to build to function tables

		X64File compiled = new X64File(this.name, staticPart);

		for (InterFunction function : functions) {
			function.compile(compiled);
		}

		return compiled;
	}

	/** Returns the offset, in bytes for the fieldName within the structure. */
	public int getFieldOffset(String fieldName) {
		return instancePart.getFieldOffset(fieldName);
	}

	public int getClassSize() {
		return instancePart.getTotalSize();
	}
}
