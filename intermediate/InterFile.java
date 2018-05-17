package intermediate;

import java.util.ArrayList;

import tree.NameNode;

public class InterFile {
    private String fileName;
    private InterStructure staticPart;
    private InterStructure instancePart;
    private ArrayList<InterStatement> staticInit;
    private ArrayList<InterStatement> instanceInit;
    private ArrayList<InterFunction> functions;

    private ArrayList<NameNode> implementsNodes;
    private NameNode superNode;
    
    /**
     * Creates an intermediate file, given the file's name
     * @param fileName The name, such as java.util.Scanner
     */
    public InterFile(String fileName) {
    	this.fileName = fileName;
    	this.staticPart = new InterStructure(false);
    	this.instancePart = new InterStructure(true);
    	this.staticInit = new ArrayList<InterStatement>();
    	this.instanceInit = new ArrayList<InterStatement>();
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
    
    /**
     * Adds an intermediate node statement to the list of static initializers.
     * @param statement An instance of a subclass of InterStatement to add.
     */
    public void addStaticInit(InterStatement statement) {
    	this.staticInit.add(statement);
    }
    
    /**
     * Adds an intermediate node statement to the list of
     *  instance initializers.
     * This code is what's shared between all constructors
     * @param statement An instance of a subclass of InterStatement to add.
     */
    public void addInstanceInit(InterStatement statement) {
		this.instanceInit.add(statement);
	}

	/** Gets the file's name (no path part) as a String.
	 */
	public String getFileName() {
		return fileName + ".jil";
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("// file: ");
		result.append(fileName);
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
		
		// static init
		if (staticInit.size() != 0) {
			result.append("init static\n");
			for (InterStatement i : staticInit) {
				result.append('\t');
				result.append(i.toString());
				result.append('\n');
			}
			result.append("end init static\n");
		}
		
		// instance init (shared between all constructors)
		if (instanceInit.size() != 0) {
			result.append("init instance\n");
			for (InterStatement i : instanceInit) {
				result.append('\t');
				result.append(i.toString());
				result.append('\n');
			}
			result.append("end init instance\n");
		}
		
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

	
}
