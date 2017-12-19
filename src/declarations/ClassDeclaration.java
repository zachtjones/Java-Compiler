package declarations;

import java.io.IOException;
import java.util.ArrayList;

import tokens.ClassToken;
import tokens.KeywordToken;
import tokens.KeywordType;
import tokens.LeftParentheses;
import tokens.Token;
import tokens.Semicolon;

public class ClassDeclaration {
	public ClassToken reference;
	public ArrayList<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	public ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	public ArrayList<MethodDeclaration> constructors = new ArrayList<MethodDeclaration>();

	
	public ClassDeclaration(ClassToken reference, int startSeq, int endSeq, ArrayList<Token> tokenSeq) throws IOException {
		this.reference = reference;
		for (int i = startSeq; i < endSeq; i++) {
			// go until you reach either:
			//  - semicolon, which means you are reading a field
			//  - open parentheses, which means you are reading a method, in that case go until the end of the block.
			boolean reachedSemi = false;
			boolean reachedParen = false;
			for (int j = i; !reachedSemi && !reachedParen; j++) {
				if (tokenSeq.get(j) instanceof Semicolon) {
					reachedSemi = true;
					this.fields.add(new FieldDeclaration(this.reference, i, tokenSeq));
					i = j;
					break;
				} else if (tokenSeq.get(j) instanceof LeftParentheses) {
					reachedParen = true;
					MethodDeclaration md = new MethodDeclaration(this.reference, i, tokenSeq);
					if (md.isConstructor) {
						this.constructors.add(md);
					} else {
						this.methods.add(md);
					}
					i = md.endIndex; // move to the next thing.
				} else if (tokenSeq.get(j) instanceof KeywordToken) {
					if (((KeywordToken)(tokenSeq.get(j))).t == KeywordType.CLASS ) {
						// nested class -- complicated, so ignoring for now TODO
						// really just have to consider this a class declaration, and add an instance of the inner class
						//  to the outer one if not static
						throw new IOException("Nested classes not implemented");
					}
				}
			}
		}
		// TODO
	}
}
