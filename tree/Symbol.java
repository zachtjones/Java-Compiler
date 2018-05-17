package tree;

public class Symbol {
	/// the allowed types of the symbol
	public final static int CLASS = 0;
	public final static int FIELD = 1;
	public final static int FUNCT = 2;
	public final static int PARAM = 3;
	public final static int LOCAL = 4;
	
	/** One of the Symbol class constants. */
	public int type;
	/** The name (the identifier) corresponding to this symbol. */
	public String name;
	
}
