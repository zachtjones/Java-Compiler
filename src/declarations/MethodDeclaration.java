package declarations;

import java.io.IOException;
import java.util.ArrayList;

import parser.Parser;
import tokens.ClassToken;
import tokens.CommaToken;
import tokens.KeywordToken;
import tokens.KeywordType;
import tokens.LeftParentheses;
import tokens.RightParentheses;
import tokens.Semicolon;
import tokens.Symbol;
import tokens.Token;
import tokens.assignment.EqualsToken;
import declarations.statements.Assignment;
import declarations.statements.Declaration;
import declarations.statements.DeclarationAssignment;
import declarations.statements.DoWhileStatement;
import declarations.statements.Expression;
import declarations.statements.IfStatement;
import declarations.statements.ReturnStatement;
import declarations.statements.WhileStatement;

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
	 * @throws IOException 
	 */
	private static ArrayList<Statement> parseBlock(int start, int end, ArrayList<Token> tokenSeq) throws IOException{
		ArrayList<Statement> results = new ArrayList<Statement>();
		for (int i = start; i <= end; i++) {
			Token currToken = tokenSeq.get(i);
			if (currToken instanceof KeywordToken) {
				// if, while, do, try, switch, for
				KeywordToken kt = (KeywordToken) currToken;
				switch(kt.t) {
				case IF:// if (condition) { block }
					IfStatement is = new IfStatement();
					int endCondition = Parser.getMatchingParen(i + 2, tokenSeq) - 1;
					is.expression = parseExpression(i + 2, endCondition, tokenSeq);
					int endBlock = Parser.getMatchingParen(endCondition + 2, tokenSeq);
					is.block = parseBlock(endCondition + 2, endBlock - 1, tokenSeq);
					results.add(is);
					i = endBlock;
					break;
				case WHILE:
					WhileStatement ws = new WhileStatement();
					endCondition = Parser.getMatchingParen(i + 2, tokenSeq) - 1;
					ws.expression = parseExpression(i + 2, endCondition, tokenSeq);
					endBlock = Parser.getMatchingParen(endCondition + 2, tokenSeq);
					ws.block = parseBlock(endCondition + 2, endBlock - 1, tokenSeq);
					results.add(ws);
					i = endBlock;
					break;
				case DO: // do { statement(s) } while (expression);
					DoWhileStatement ds = new DoWhileStatement();
					endBlock = Parser.getMatchingBrace(i + 2, tokenSeq);
					ds.block = parseBlock(i + 2, endBlock - 1, tokenSeq);
					endCondition = Parser.getMatchingParen(endBlock + 2, tokenSeq);
					ds.expression = parseExpression(endBlock + 2, endCondition - 1, tokenSeq);
					results.add(ds);
					i = endBlock + 1; // condition );
					break;
				case TRY:
					throw new IOException("Try parsing not implemented yet.");
				case SWITCH:
					throw new IOException("Switch parsing not implemented yet.");
				case FOR: // for ( ; ; ) { block } or for ( : ) { block } 
					throw new IOException("For loops not implemented yet.");
				default:
					// not if, while, do, try, or switch
					int semiIndex = getSemicolon(i, tokenSeq);
					results.add(parseStatement(i, semiIndex - 1, tokenSeq));
					i = semiIndex;
					break;
				}
				
			} else {
				// find the next semicolon
				int semiIndex = getSemicolon(i, tokenSeq);
				results.add(parseStatement(i, semiIndex - 1, tokenSeq));
				i = semiIndex;
			}
		}
		
		return results;
	}
	/**
	 * Finds the next semicolon in the tokenSeq
	 * @param start The starting index to search.
	 * @param tokenSeq The sequence of tokens
	 * @return The index of the next semicolon found, or -1 if not found.
	 * @throws IOException 
	 */
	private static int getSemicolon(int start, ArrayList<Token> tokenSeq) throws IOException {
		for (int i = start; i < tokenSeq.size(); i++) {
			if (tokenSeq.get(i) instanceof Semicolon) {
				return i;
			}
		}
		throw new IOException("Semicolon index not found.");
	}
	
	/**
	 * Parses a statement. (inclusive range)
	 * @param start The start index to look from.
	 * @param end The end index to go until. (this is the token before the semicolon)
	 * @param tokenSeq The sequence of tokens.
	 * @return The statement parsed.
	 * @throws IOException 
	 */
	private static Statement parseStatement(int start, int end, ArrayList<Token> tokenSeq) throws IOException {
		// of the form: declaration, declaration = expression, assignment = expression, return expression
		boolean containsEquals = false;
		for (int i = start; i < end; i++) {
			if (tokenSeq.get(i) instanceof EqualsToken) {
				containsEquals = true;
			}
		}
		if (containsEquals) { // either declaration assignment, or plain assignment
			if (tokenSeq.get(start) instanceof ArgType) { // declaration assignment
				DeclarationAssignment da = new DeclarationAssignment();
				da.type = (ArgType) tokenSeq.get(start);
				da.name = ((Symbol) tokenSeq.get(start + 1)).contents;
				da.assingment = parseExpression(start + 3, end, tokenSeq);
				return da;
			} else { // plain assignment
				Assignment a = new Assignment();
				a.name = ((Symbol) tokenSeq.get(start)).contents;
				a.assingment = parseExpression(start + 2, end, tokenSeq);
				return a;
			}
		}
		// can be declaration, or return expression
		if (tokenSeq.get(start) instanceof KeywordToken) {
			KeywordToken kt = (KeywordToken) tokenSeq.get(start);
			if (kt.t == KeywordType.RETURN) {
				if (end == start + 1) {
					// return;
					return new ReturnStatement();
				} else {
					// return <expression>;
					ReturnStatement rs = new ReturnStatement();
					rs.expression = parseExpression(start + 1, end, tokenSeq);
					return rs;
				}
			}
		}
		// can only be a declaration at this point
		// type name;
		Declaration d = new Declaration();
		d.type = (ArgType) tokenSeq.get(start);
		d.name = ((Symbol) tokenSeq.get(start + 1)).contents;
		return d;
	}
	/**
	 * Parses an expression. (inclusive range)
	 * @param start The start index to look from.
	 * @param end The end index to go until. (this is the token before the semicolon)
	 * @param tokenSeq The sequence of tokens.
	 * @return The expression parsed.
	 */
	private static Expression parseExpression(int start, int end, ArrayList<Token> tokenSeq) {
		System.out.print("Expression: ");
		for (int i = start; i <= end; i++) {
			System.out.print(tokenSeq.get(i) + " ");
		}
		System.out.println();
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
