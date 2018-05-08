package intermediate;

import java.util.ArrayList;

public class InterFile {
    private String fileName;
    private InterStructure staticPart;
    private InterStructure instancePart;
    private ArrayList<InterStatement> staticInit;
    
    /**
     * Creates an intermediate file, given the file's name
     * @param fileName The name, such as java.util.Scanner
     */
    public InterFile(String fileName) {
    	this.fileName = fileName;
    	this.staticPart = new InterStructure(false);
    	this.instancePart = new InterStructure(true);
    	this.staticInit = new ArrayList<InterStatement>();
    }
    
    /**
     * Adds a field to the intermediate file
     * @param type The string that is the type of the field
     * @param name The identifier of the field
     * @param isStatic true if the field is static, 
     * false if it is instance-based.
     */
    public void addField(String type, String name, boolean isStatic) {
    	if (isStatic) {
    		this.staticPart.addMember(type, name);
    	} else {
    		this.instancePart.addMember(type, name);
    	}
    }
    
    /**
     * Adds an intermediate node statement to the list of static initializers.
     * @param statement An instance of a subclass of InterStatement to add.
     */
    public void addStaticInit(InterStatement statement) {
    	this.staticInit.add(statement);
    }

	/** Gets the file's name (no path part) as a String.
	 */
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("// file: ");
		result.append(fileName);
		result.append(".jil\n\n");
		
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
		
		// functions
		// TODO
		
		return result.toString();
	}
}
