package declarations;

import java.util.ArrayList;

import tokens.ClassToken;
import tokens.Token;

public class ClassDeclaration {
	public ClassToken reference;
	public ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	public ArrayList<MethodDeclaration> constructors = new ArrayList<MethodDeclaration>();
	
	public ClassDeclaration(ClassToken reference, int startSeq, int endSeq, ArrayList<Token> tokenSeq) {
		this.reference = reference;
		// TODO
	}
}
