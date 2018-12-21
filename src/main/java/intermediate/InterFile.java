package intermediate;

import java.util.ArrayList;

import amd64.AMD64File;
import helper.CompileException;
import tree.NameNode;
import x86.X86_64File;

public class InterFile {
	private String name;
	private InterStructure staticPart;
	private InterStructure instancePart;
	private ArrayList<InterFunction> functions;

	private ArrayList<NameNode> implementsNodes;
	private NameNode superNode;

	/**
	 * Creates an intermediate file, given the name
	 * @param fileName The name, such as java/util/Scanner
	 */
	public InterFile(String name) {
		this.name = name;
		this.staticPart = new InterStructure(false);
		this.instancePart = new InterStructure(true);
		this.functions = new ArrayList<InterFunction>();
	}

	/**
	 * Adds a field to the intermediate file
	 * @param type The string that is the type of the field
	 * @param name The identifier of the field
	 * @param isStatic true if the field is static, 
	 * false if it is instance-based.
	 */
	public void addField(String type, String name, boolean isStatic) {
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
	public void addField(String type, String name, boolean isStatic, String value) {
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
		if (functions.size() != 0) {
			for (InterFunction f : functions) {
				result.append(f.toString());
			}
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
			InterFunction d = new InterFunction();
			// add the name and return type
			d.name = "<init>";
			d.isInstance = true;
			d.returnType = fileName.replace(".jil", "");
			
			// add the only statement, super();
			d.statements = new ArrayList<InterStatement>();
			CallActualStatement c;
			Register[] args = new Register[0];
			
			// load this pointer (call super on this)
			RegisterAllocator ra = new RegisterAllocator();
			Register thisPointer = ra.getNext(Register.REFERENCE);
			d.statements.add(new GetParamStatement(thisPointer, "this", fileName, line));
			//  superclass of this object.
			if (superNode != null) {
				// add in the call to it's init
				c = new CallActualStatement(thisPointer, superNode.primaryName, "<init>", args, null,
						fileName, line);
			} else {
				c = new CallActualStatement(thisPointer, "java/lang/Object", "<init>", args, null,
						fileName, line);
			}
			d.statements.add(c);
			functions.add(d);
		}
	}

	/** Type checks all the functions 
	 * @throws CompileException If there is an error with type checking.*/
	public void typeCheck() throws CompileException {
		
		for (InterFunction f : functions) {
			f.typeCheck(name);
		}
	}

	/**
	 * Gets the type of the field for this structure
	 * @param fieldName The field's name
	 * @return The JIL representation of the type
	 * @throws CompileException if the field doesn't exist, or there is a problem checking it.
	 */
	public String getInstFieldType(String fieldName, String fileName, int line) throws CompileException {
		return instancePart.getFieldType(fieldName, fileName, line);
	}

	/**
	 * Gets the type of the field for this class static field.
	 * @param fieldName The field's name
	 * @return The JIL representation of the type
	 * @throws CompileException if the field doesn't exist, or there is a problem checking it.
	 */
	public String getStatFieldType(String fieldName, String fileName, int line) throws CompileException {
		return staticPart.getFieldType(fieldName, fileName, line);
	}

	/**
	 * Finds the method with the signature given, and returns the type of the return object
	 * @param name The method's name.
	 * @param args The array of arguments.
	 * @return The return object type, or null if there's no method with that signature
	 */
	public String getReturnType(String name, Register[] args) {
		for (InterFunction f : functions) {
			if (f.name.equals(name) && f.paramTypes.size() == args.length) {
				boolean matchedAll = true;
				for (int i = 0; i < args.length; i++) {
					if (!args[i].typeFull.equals(f.paramTypes.get(i))) {
						matchedAll = false;
					}
				}
				if (matchedAll) {
					return f.returnType;
				}
			}
			// TODO the ... on args and inheritance
		}
		return null;
	}

	public X86_64File compileX86_64() throws CompileException {
		throw new CompileException("x86-64 assembly not implemented yet", this.toString(), -1);
	}
}
