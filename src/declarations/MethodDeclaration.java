package declarations;

import java.io.IOException;
import java.util.ArrayList;

import parser.Parser;
import tokens.ClassToken;
import tokens.CommaToken;
import tokens.KeywordToken;
import tokens.KeywordType;
import tokens.LeftCurlyBrace;
import tokens.LeftParentheses;
import tokens.RightCurlyBrace;
import tokens.RightParentheses;
import tokens.Semicolon;
import tokens.Symbol;
import tokens.Token;
import declarations.statements.Expression;
import declarations.statements.IfStatement;

public class MethodDeclaration {
	
	public ClassToken className;
	public String name;
	public ArgType type; // return type
	public ArrayList<Parameter> params = new ArrayList<Parameter>();
	public ArrayList<Statement> statements;
	public ArrayList<ClassToken> thrownClasses;
	public boolean isStatic = false;
	public boolean isConstructor = false;
	public boolean isAbstract = false;
	
	public int endIndex; // the end index in the token sequence.
	
	public MethodDeclaration(ClassToken className, int start, ArrayList<Token> tokenSeq) throws IOException {
		this.className = className;
		// resolve static, constructor, name, abstract
		while (tokenSeq.get(start) instanceof KeywordToken) {
			KeywordToken t = (KeywordToken) tokenSeq.get(start);
			if (t.t == KeywordType.STATIC) {
				isStatic = true;
				start++;
			} else if (t.t == KeywordType.ABSTRACT) {
				isAbstract = true;
				start++;
			} else {
				// primitive return type -- break
				break;
			}
		}
		
		// start is the position of the name (constructor) or the return type
		Token t = tokenSeq.get(start);
		if (t instanceof ClassToken) {
			ClassToken ct = (ClassToken)t;
			if (ct.equals(this.className)) {
				// is either a method that returns this class, or a constructor
				if (tokenSeq.get(start + 1) instanceof LeftParentheses) {
					// constructor
					this.isConstructor = true;
				} else {
					start++; // move start to method name
				}
			} else {
				start++;
			}
			this.type = ct;
		} else {
			// primitive return type
			this.type = (ArgType) t; // either int, boolean, ... , or void
			start++; // move start to method name
		}
		
		// start is the position of the name
		if (!this.isConstructor) {
			this.name = ( (Symbol) tokenSeq.get(start)).contents; // leave name null for constructors
		}
		start += 2; // advance past name and opening parenthesis
		
		// start is the position of the first argument's type (or final)
		while(!(tokenSeq.get(start) instanceof RightParentheses)) {
			Parameter temp = new Parameter();
			Token tempToken = tokenSeq.get(start);
			if (tempToken instanceof KeywordToken && ((KeywordToken) tempToken).t == KeywordType.FINAL) {
				temp.isFinal = true;
				start++;
			}
			temp.type = (ArgType) tokenSeq.get(start);
			start++;
			temp.name = ( (Symbol) tokenSeq.get(start)).contents;
			start ++; // advance past name
			if (tokenSeq.get(start) instanceof CommaToken) { start ++; }
		}
		// at right parenthesis
		start++;
		
		// either at opening brace or semicolon (abstract methods)
		if (tokenSeq.get(start) instanceof Semicolon) {
			this.endIndex = start;
			if (!this.isAbstract) {
				throw new IOException("Method: " + this.name + " of class " + this.className + " is not declared abstract but has no body.");
			}
			return;
		}
		start++;
		
		// start is at the first statement's start, or closing brace for method if body is empty
		this.endIndex = Parser.getMatchingBrace(start, tokenSeq);
		this.statements = parseBlock(start, this.endIndex - 1, tokenSeq); // parse the body of this method
	}
	
	/**
	 * Parses a block into a sequence of statements.
	 * @param start The start index to look from.
	 * @param end The end index to go until. (this is the token before the right curly brace)
	 * @param tokenSeq The sequence of tokens.
	 * @return The statement parsed.
	 */
	private static ArrayList<Statement> parseBlock(int start, int end, ArrayList<Token> tokenSeq){
		ArrayList<Statement> results = new ArrayList<Statement>();
		for (int i = start; i <= end; i++) {
			Token currToken = tokenSeq.get(i);
			if (currToken instanceof KeywordToken) {
				// if, while, do, try, switch
				KeywordToken kt = (KeywordToken) currToken;
				switch(kt.t) {
				case IF:// if (condition) { block }
					IfStatement is = new IfStatement();
					int endCondition = Parser.getMatchingParen(i + 2, tokenSeq) - 1;
					is.expression = parseExpression(i + 2, endCondition, tokenSeq);
					int endBlock = Parser.getMatchingParen(endCondition + 2, tokenSeq);
					is.block = parseBlock(endCondition + 2, endBlock - 1, tokenSeq);
				case WHILE:
					//TODO
				case DO:
				case TRY:
				case SWITCH:
					
					break;
				default:
					// not if, while, do, try, or switch
					int semiIndex = getSemicolon(i, tokenSeq);
					results.add(parseStatement(i, semiIndex - 1, tokenSeq));
					i = semiIndex + 1;
				}
				
			} else {
				// find the next semicolon
				int semiIndex = getSemicolon(i, tokenSeq);
				results.add(parseStatement(i, semiIndex - 1, tokenSeq));
				i = semiIndex + 1;
			}
		}
		
		return results;
	}
	/**
	 * Finds the next semicolon in the tokenSeq
	 * @param start The starting index to search.
	 * @param tokenSeq The sequence of tokens
	 * @return The index of the next semicolon found, or -1 if not found.
	 */
	private static int getSemicolon(int start, ArrayList<Token> tokenSeq) {
		for (int i = start; i < tokenSeq.size(); i++) {
			if (tokenSeq.get(i) instanceof Semicolon) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Parses a statement.
	 * @param start The start index to look from.
	 * @param end The end index to go until. (this is the token before the semicolon)
	 * @param tokenSeq The sequence of tokens.
	 * @return The statement parsed.
	 */
	private static Statement parseStatement(int start, int end, ArrayList<Token> tokenSeq) {
		// TODO
		return null;
	}
	/**
	 * Parses an expression.
	 * @param start The start index to look from.
	 * @param end The end index to go until. (this is the token before the semicolon)
	 * @param tokenSeq The sequence of tokens.
	 * @return The expression parsed.
	 */
	private static Expression parseExpression(int start, int end, ArrayList<Token> tokenSeq) {
		// TODO
		return null;
	}
}

class Parameter {
	ArgType type;
	String name;
	boolean isFinal;
	
	public Parameter() {
		this.type = null;
		this.name = null;
		this.isFinal = false;
	}
}
