package tokens;

import declarations.ArgType;

public class ClassTokenArray implements Token, ArgType {
	public ClassToken classToken;
	public int dimension;
	
	public ClassTokenArray(ClassToken classToken, int dimension) {
		this.classToken = classToken;
		this.dimension = dimension;
	}

	public String toString() {
		return dimension + "- d " + classToken.toString();
	}
}
