package declarations;

import java.util.ArrayList;

import tokens.ClassToken;
import tokens.Symbol;
import tokens.Token;

public class FieldDeclaration {
	public ArgType type;
	public String name;
	
	public FieldDeclaration(ClassToken reference, int start, ArrayList<Token> tokenSeq) {
		// should be the type at start and the name at start + 1
		this.type = (ArgType) tokenSeq.get(start);
		this.name = ((Symbol)(tokenSeq.get(start + 1))).contents;
	}
	
	public String toString() {
		return "type = '" + type + "', name = '" + name + "'";
	}
}
