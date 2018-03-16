package declarations;

import java.io.IOException;
import java.util.ArrayList;

import declarations.statements.ArrayInitializerSize;
import declarations.statements.ConstructorCall;
import declarations.statements.Expression;
import declarations.statements.MethodCall;
import declarations.statements.PostDecrement;
import declarations.statements.PostIncrement;
import declarations.statements.PreDecrement;
import declarations.statements.PreIncrement;
import declarations.statements.PropertyAccess;
import declarations.statements.Field;
import parser.Parser;
import tokens.AdditionToken;
import tokens.ClassToken;
import tokens.ClassTokenArray;
import tokens.CommaToken;
import tokens.DecrementToken;
import tokens.DotToken;
import tokens.IncrementToken;
import tokens.KeywordToken;
import tokens.KeywordType;
import tokens.LeftParentheses;
import tokens.MinusToken;
import tokens.NumberToken;
import tokens.RightParentheses;
import tokens.StringToken;
import tokens.Symbol;
import tokens.Token;

public class ExpressionParser {
	
	private static int expressionUnparsedCount = 0;
	
	/** Parses a single token expression */
	private static Expression parseSingle(int start, ArrayList<Token> tokenSeq) {
		// either a constant value or a symbol
		if (tokenSeq.get(start) instanceof Symbol) {
			return (Symbol) tokenSeq.get(start);
		} else if (tokenSeq.get(start) instanceof NumberToken) {
			return (NumberToken) tokenSeq.get(start);
		} else if (tokenSeq.get(start) instanceof StringToken) {
			return (StringToken) tokenSeq.get(start);
		}
		return null;
	}
	
	private static Expression parseDouble(int start, ArrayList<Token> tokenSeq) {
		// can be: i++, i--, ++i, --i, -i, +i (unary minus and plus operators)
		// unary + does nothing.
		if (tokenSeq.get(start) instanceof IncrementToken) { // ++i;
			return new PreIncrement((Symbol) tokenSeq.get(start + 1));
		}
		if (tokenSeq.get(start + 1) instanceof IncrementToken) { // i++;
			return new PostIncrement((Symbol) tokenSeq.get(start));
		}
		if (tokenSeq.get(start) instanceof DecrementToken) { // --i;
			return new PreDecrement((Symbol) tokenSeq.get(start + 1));
		}
		if (tokenSeq.get(start + 1) instanceof IncrementToken) { // i--;
			return new PostDecrement((Symbol) tokenSeq.get(start));
		}
		if (tokenSeq.get(start) instanceof MinusToken) { //unary minus (negative sign)
			NumberToken nt = (NumberToken) tokenSeq.get(start + 1);
			nt.negate();
			return nt;
		}
		if (tokenSeq.get(start) instanceof AdditionToken) { // unary plus (ignore)
			return (NumberToken) tokenSeq.get(start + 1);
		}
		return null;
	}

	/**
	 * Parses an expression. (inclusive range)
	 * @param start The start index to look from.
	 * @param end The end index to go until. (this is the token before the semicolon)
	 * @param tokenSeq The sequence of tokens.
	 * @return The expression parsed.
	 * @throws IOException 
	 */
	public static Expression parseExpression(int start, int end, ArrayList<Token> tokenSeq) throws IOException {
		if (end == start) { // length 1 expression
			return parseSingle(start, tokenSeq);
		} else if (end == start + 1) { // length 2 expression
			return parseDouble(start, tokenSeq);
		}
		
		// function calls: NAME(args)
		if (tokenSeq.get(start + 1) instanceof LeftParentheses) {
			MethodCall m = new MethodCall();
			m.name = (Symbol) tokenSeq.get(start);
			// ending parenthesis
			int matching = Parser.getMatchingParen(start + 2, tokenSeq);
			m.args = parseArgs(start + 2, matching - 1);
			if (matching == end) {
				return m;
			}
			// TODO method call is part of expression
			// could be . or any math expression
		}
		
		
		// since the length > 2, there could be complicated expressions.
		// highest preference: parentheses, accessing array elements, or dots
		if (tokenSeq.get(start + 1) instanceof DotToken) {
			// could be property access or function
			// property access is length 3
			if (end == start + 2) {
				// property access
				return new PropertyAccess( (Symbol)tokenSeq.get(start),
						(Symbol)tokenSeq.get(end));
			} else {
				// function call
				MethodCall m = new MethodCall();
				m.object = tokenSeq.get(start);
				// find the method call with the rest of the expression.
				MethodCall rest = (MethodCall) parseExpression(start + 2, end, tokenSeq);
				m.args = rest.args;
				return m;
			}
		}
		
		
		
		
		// https://introcs.cs.princeton.edu/java/11precedence/
		
		
		// TODO order of operations
		/*
		// resolve calls to constructors
		if (tokenSeq.get(start) instanceof KeywordToken) {
			KeywordToken kt = (KeywordToken) tokenSeq.get(start);
			if (kt.t == KeywordType.NEW) { // new CLASS() or new CLASS (thing1, thing2, ...)
				// start + 2 should be either a ( or a [
				// bracket is for array init
				if (tokenSeq.get(start + 2) instanceof LeftParentheses) {
					if (Parser.getMatchingParen(start + 3, tokenSeq) == end) { // only the constructor call, 
						// otherwise, have to break down into a compound expression.
						// calling a constructor
						ConstructorCall cc = new ConstructorCall();
						cc.classToken = (ClassToken) tokenSeq.get(start + 1);
						if (tokenSeq.get(start + 3) instanceof RightParentheses) {
							return cc; // done, no parameters, empty array list of arguments
						}
						// can have 1 or more arguments now
						for (int i = start + 3; i <= end; i++) { // the arguments to the constructor call
							// build up each argument (go until comma or end)
							int endArg = i;
							do {
								endArg++;
							} while (!(tokenSeq.get(endArg) instanceof CommaToken) && endArg != end);
							// endArg is the end of the particular argument .. either comma or )
							cc.parameters.add(parseExpression(i, endArg - 1, tokenSeq));
							i = endArg + 1;
						}
						return cc;
					} else {
						// constructor call part of expression (ex. new Random().next() )
						throw new IOException("Constructor call part of expressions not implemented yet.");
					}
				} else { // array init..     new TYPE[ expression1 ] [ expression2 ] 
					// the catch here is that the type will be captured into a 1-d classTokenArray.
					ArrayInitializerSize ais = new ArrayInitializerSize();
					ClassTokenArray cta = (ClassTokenArray) tokenSeq.get(start + 1);
					ais.type = cta.classToken;
					start += 2; // move to start of first expression part
					while (start < end) {
						// find the location of the matching ]
						int endExpression = Parser.getMatchingBracket(start, tokenSeq);
						ais.bounds.add(parseExpression(start, endExpression - 1, tokenSeq));
						start = endExpression + 2;
					}
					return ais;
				}
				
			}
		}
		
		// resolve method calls / fields
		Token first = tokenSeq.get(start);
		Token second = tokenSeq.get(start + 1);
		Token third = tokenSeq.get(start + 2);
		if ((first instanceof Symbol || first instanceof ClassToken) 
				&& second instanceof DotToken && third instanceof Symbol) {
			
			// either method call or field
			if (end > start + 2 && tokenSeq.get(start + 3) instanceof LeftParentheses) {
				// method call
				MethodCall mc = new MethodCall();
				mc.object = first;
				mc.name = (Symbol) third;
				start += 4; // move to start of first expression part for args
				int endExpression = Parser.getMatchingParen(start, tokenSeq);
				while (start <= endExpression) {
					// go until the commas
					for (int j = start; j < end; j++) {
						if (tokenSeq.get(j) instanceof CommaToken) {
							// expression is from start to j - 1
							mc.args.add(parseExpression(start, j - 1, tokenSeq));
							start = j + 1;
							break;
						}
					}
					if (start <= endExpression) {
						// add the last argument
						mc.args.add(parseExpression(start, end - 1, tokenSeq));
						break;
					}
				}
				return mc;
			} else {
				// field
				Field field = new Field();
				field.object = first;
				field.name = (Symbol) third;
				return field;
			}
		}
		*/
		
		expressionUnparsedCount++;
		System.out.print("Expression # " + expressionUnparsedCount + ": ");
		for (int i = start; i <= end; i++) {
			System.out.print(tokenSeq.get(i) + " ");
		}
		System.out.println();
		// TODO
		return null;
	}

	private static ArrayList<Expression> parseArgs(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}
}
