package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

import declarations.ClassDeclaration;
import tokens.AdditionToken;
import tokens.BitNotToken;
import tokens.BitwiseAndToken;
import tokens.BitwiseOrToken;
import tokens.BitwiseXOrToken;
import tokens.ClassToken;
import tokens.ClassTokenArray;
import tokens.ColonToken;
import tokens.CommaToken;
import tokens.DecrementToken;
import tokens.DivideToken;
import tokens.GreaterThanEqualToken;
import tokens.assignment.BitAndEqualsToken;
import tokens.assignment.BitOrEqualsToken;
import tokens.assignment.BitXorEqualsToken;
import tokens.assignment.DivideEqualsToken;
import tokens.assignment.DoubleEqualsToken;
import tokens.assignment.EqualsToken;
import tokens.assignment.MinusEqualsToken;
import tokens.assignment.ModulusEqualsToken;
import tokens.assignment.PlusEqualsToken;
import tokens.GreaterThanToken;
import tokens.IncrementToken;
import tokens.KeywordToken;
import tokens.KeywordType;
import tokens.LeftBracket;
import tokens.LeftCurlyBrace;
import tokens.LeftParentheses;
import tokens.LeftShiftToken;
import tokens.LessThanEqualToken;
import tokens.LessThanToken;
import tokens.LogicalAndToken;
import tokens.LogicalOrToken;
import tokens.MinusToken;
import tokens.ModulusToken;
import tokens.NumberToken;
import tokens.QuestionToken;
import tokens.RightBracket;
import tokens.RightCurlyBrace;
import tokens.RightParentheses;
import tokens.RightShift0Extend;
import tokens.RightShiftSignExtend;
import tokens.Semicolon;
import tokens.StringToken;
import tokens.Symbol;
import tokens.Token;

public class Parser {

	private static final String WHITESPACE = "\r|\n|\f|\t| ";
	private static final String LIBPATH = "lib/";

	public static ParserResult parse(String file) throws IOException {
		// 1st pass, generate token sequence and resolve keywords
		ParserResult result = generateTokens(file);
		// 2nd pass, convert tokens to ClassTokens
		result.contents = classifyTokens(result, file);
		// 3rd pass, convert complex operations (+=, ...)
		result.contents = groupOperators(result);
		// 4th pass, convert ClassTokens to classes
		groupTokens(result);

		return result;
	}

	private static ParserResult generateTokens(String file) throws IOException {
		String contents = new String(Files.readAllBytes(Paths.get(file)));
		contents = contents.replaceAll("//.*?\n", "\n"); //replace line comments
		contents = contents.replaceAll("/\\*[\\s\\S]*?\\*/", ""); // replace block comments
		StringTokenizer st = new StringTokenizer(contents, " \t\r\n\f\"[]{}();.,=+-/%:~<>&|^!?", true);
		ParserResult result = new ParserResult();

		while (st.hasMoreTokens()) {
			String token = st.nextToken();

			// handle strings
			if (StringToken.matches(token)) {
				StringToken tok = new StringToken(st); //grabs the string contents
				result.contents.add(tok);
			}
			// ignore whitespace
			else if (token.matches(WHITESPACE)) {
				continue;
			}
			// keywords
			else if (KeywordToken.matches(token)) {
				KeywordToken kd = new KeywordToken(token);
				if (kd.t == null) {
					System.out.println("token is: " + token);
					kd.t.toString();
				}
				// handle package declaration
				if (kd.t == KeywordType.PACKAGE) {
					String temp;
					while (!(temp = st.nextToken()).equals(";")) {
						if (!temp.matches(WHITESPACE)) {
							// it is the package declaration (or part of for ex: java.lang.String)
							result.packageId += temp;
						}
					}
					continue;
				} else if (kd.t == KeywordType.IMPORT) {
					// handle import statements
					String total = "";
					String temp;
					while (!(temp = st.nextToken()).equals(";")) {
						if (!temp.matches(WHITESPACE)) {
							total += temp;
						}
					}
					result.imports.add(total);
					continue;
				}
				result.contents.add(new KeywordToken(token));
			}
			else {
				// language punctuation
				if (LeftCurlyBrace.matches(token)) {
					result.contents.add(LeftCurlyBrace.getInstance());
				} else if (RightCurlyBrace.matches(token)) {
					result.contents.add(RightCurlyBrace.getInstance());
				} else if (Semicolon.matches(token)) {
					result.contents.add(Semicolon.getInstance());
				} else if (LeftParentheses.matches(token)) {
					result.contents.add(LeftParentheses.getInstance());
				} else if (RightParentheses.matches(token)) {
					result.contents.add(RightParentheses.getInstance());
				} else if (LeftBracket.matches(token)) {
					result.contents.add(LeftBracket.getInstance());
				} else if (RightBracket.matches(token)) {
					result.contents.add(RightBracket.getInstance());
				} else if (EqualsToken.matches(token)) {
					result.contents.add(EqualsToken.getInstance());
				} else if (AdditionToken.matches(token)) {
					result.contents.add(AdditionToken.getInstance());
				} else if (MinusToken.matches(token)) {
					result.contents.add(MinusToken.getInstance());
				} else if (DivideToken.matches(token)) {
					result.contents.add(DivideToken.getInstance());
				} else if (ModulusToken.matches(token)) {
					result.contents.add(ModulusToken.getInstance());
				} else if (ColonToken.matches(token)) {
					result.contents.add(ColonToken.getInstance());
				} else if (BitNotToken.matches(token)) {
					result.contents.add(BitNotToken.getInstance());
				} else if (LessThanToken.matches(token)) {
					result.contents.add(LessThanToken.getInstance());
				} else if (GreaterThanToken.matches(token)) {
					result.contents.add(GreaterThanToken.getInstance());
				} else if (BitwiseAndToken.matches(token)) {
					result.contents.add(BitwiseAndToken.getInstance());
				} else if (BitwiseOrToken.matches(token)) {
					result.contents.add(BitwiseOrToken.getInstance());
				} else if (CommaToken.matches(token)) {
					result.contents.add(CommaToken.getInstance());
				} else if (BitwiseXOrToken.matches(token)) {
					result.contents.add(BitwiseXOrToken.getInstance());
				} else if (QuestionToken.matches(token)) {
					result.contents.add(QuestionToken.getInstance());
				} else if (NumberToken.matches(token)){
					result.contents.add(new NumberToken(token));
				} else {
					// generic Token for now
					result.contents.add(new Symbol(token));
				}
			}
		}
		return result;
	}

	private static ArrayList<Token> classifyTokens(ParserResult result, String file){
		// second pass, group tokens into some of their attributes
		// don't care about access modifiers -- doesn't change output code
		// want to qualify the class name tokens
		ClassToken.setup(LIBPATH, result.imports, file, result.packageId);

		ArrayList<Token> classSeq = new ArrayList<Token>();
		for (int i = 0; i < result.contents.size(); i++) {
			Token t = result.contents.get(i);
			if (t instanceof Symbol) {
				Symbol s = (Symbol)t;
				if (ClassToken.matches(s.contents)) {
					// determine if it is an array Ex. String[][]
					if (result.contents.get(i+1) instanceof LeftBracket) {
						int dimension = 1;
						for (int j = i+2; j < result.contents.size(); j++) {
							if (result.contents.get(j) instanceof LeftBracket) {
								dimension++;
							} else if (!(result.contents.get(j) instanceof RightBracket)) {								i = j;
								i = j - 1;
								break;
							}
						}
						classSeq.add(new ClassTokenArray(new ClassToken(s.contents), dimension));
					} else {
						classSeq.add(new ClassToken(s.contents));
					}
				} else {
					classSeq.add(t);
				}
			} else if(t instanceof KeywordToken) {
				KeywordToken k = (KeywordToken)t;
				switch(k.t) {
				case PUBLIC:
				case PRIVATE:
				case PROTECTED:
					break; //ignore those
				default:
					classSeq.add(t);
					break;
				}
			} else {
				classSeq.add(t); // the rest of the tokens
			}
		}
		return classSeq;
	}

	private static ArrayList<Token> groupOperators(ParserResult result) {
		ArrayList<Token> contents = result.contents;
		ArrayList<Token> results = new ArrayList<Token>(contents.size());
		for (int i = 0; i < contents.size(); i++) {
			Token currToken = contents.get(i);
			if (currToken instanceof AdditionToken) { // ++ or +=
				if (contents.get(i + 1) instanceof AdditionToken) {
					results.add(IncrementToken.getInstance());
					i++;
				} else if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(PlusEqualsToken.getInstance());
					i++;
				} else {
					results.add(AdditionToken.getInstance());
				}
			} else if (currToken instanceof MinusToken) {
				if (contents.get(i + 1) instanceof MinusToken) {
					results.add(DecrementToken.getInstance());
					i++;
				} else if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(MinusEqualsToken.getInstance());
					i++;
				} else {
					results.add(MinusToken.getInstance());
				}
			} else if (currToken instanceof EqualsToken) {
				if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(DoubleEqualsToken.getInstance());
					i++;
				} else {
					results.add(EqualsToken.getInstance());
				}
			} else if (currToken instanceof DivideToken) {
				if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(DivideEqualsToken.getInstance());
					i++;
				} else {
					results.add(DivideToken.getInstance());
				}
			} else if (currToken instanceof ModulusToken) {
				if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(ModulusEqualsToken.getInstance());
					i++;
				} else {
					results.add(ModulusToken.getInstance());
				}
			} else if (currToken instanceof LessThanToken) {
				if (contents.get(i + 1) instanceof LessThanToken) {
					results.add(LeftShiftToken.getInstance());
					i++;
				} else if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(LessThanEqualToken.getInstance());
				} else {
					results.add(LessThanToken.getInstance());
				}
			} else if (currToken instanceof GreaterThanToken) {
				if (contents.get(i + 1) instanceof GreaterThanToken) {
					if (contents.get(i + 2) instanceof GreaterThanToken) {
						results.add(RightShift0Extend.getInstance());
						i += 2;
					} else {
						results.add(RightShiftSignExtend.getInstance());
						i++;
					}
				} else if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(GreaterThanEqualToken.getInstance());
					i++;
				} else {
					results.add(GreaterThanToken.getInstance());
				}
			} else if (currToken instanceof BitwiseAndToken) {
				if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(BitAndEqualsToken.getInstance());
					i++;
				} else if (contents.get(i + 1) instanceof BitwiseAndToken) {
					results.add(LogicalAndToken.getInstance());
					i++;
				} else {
					results.add(BitwiseAndToken.getInstance());
				}
			} else if (currToken instanceof BitwiseOrToken) {
				if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(BitOrEqualsToken.getInstance());
					i++;
				} else if (contents.get(i + 1) instanceof BitwiseOrToken) {
					results.add(LogicalOrToken.getInstance());
					i++;
				} else {
					results.add(BitwiseOrToken.getInstance());
				}
			} else if (currToken instanceof BitwiseXOrToken) {
				if (contents.get(i + 1) instanceof EqualsToken) {
					results.add(BitXorEqualsToken.getInstance());
					i++;
				} else {
					results.add(BitwiseXOrToken.getInstance());
				}
			} else {
				results.add(currToken);
			}
		}
		return results;
	}
	
	private static void groupTokens(ParserResult result) throws IOException {
		ArrayList<Token> contents = result.contents;
		for (int i = 0; i < contents.size(); i++) {
			Token t = contents.get(i);
			if (t instanceof KeywordToken) {
				KeywordToken k = (KeywordToken) t;
				if (k.t == KeywordType.CLASS) {
					// class NAME extends SUPERCLASS implements I1, I2
					ClassToken name = (ClassToken) contents.get(i + 1);
					int startBlockIndex = -1;
					boolean implementsStart = false;
					for (int j = i; j < contents.size(); j++) {
						Token temp = contents.get(j);
						if (temp instanceof LeftCurlyBrace) {
							startBlockIndex = j + 1;
							break;
						} else if (temp instanceof KeywordToken && ((KeywordToken)(temp)).t == KeywordType.EXTENDS) {
							ClassToken c = (ClassToken) contents.get(j + 1);
							result.inheritances.put(name, c); // name extends c
						} else if (temp instanceof KeywordToken && ((KeywordToken)(temp)).t == KeywordType.IMPLEMENTS) {
							implementsStart = true;
							continue;
						}
						if (implementsStart) {
							ClassToken c = (ClassToken) temp;
							if (!result.implementations.containsKey(name)) {
								result.implementations.put(name, new ArrayList<ClassToken>());
							}
							result.implementations.get(name).add(c); // name implements c, where name isn't already in map
						}
					}
					if (startBlockIndex == -1) {
						throw new IOException("No block found for class declaration: " + name);
					} 
					int endBlockIndex = getMatchingBrace(startBlockIndex, contents);
					if (endBlockIndex == -1) { 
						throw new IOException("No end curly brace found for class declaration: " + name); 
					} 
					result.classes.add(new ClassDeclaration(name, startBlockIndex, endBlockIndex, result.contents));
				} else if (k.t == KeywordType.INTERFACE) {
					// TODO
				} else if (k.t == KeywordType.ENUM) {
					// TODO
				}
			}
		}
	}
	/**
	 * Gets the index of the closing brace for the block. 
	 * The starting point of the block is the index after the opening (left) brace.
	 */
	public static int getMatchingBrace(int startBlock, ArrayList<Token> contents) {
		int count = 0;
		for (int i = startBlock; i < contents.size(); i++) {
			boolean isRightCurly = contents.get(i) instanceof RightCurlyBrace;
			boolean isLeftCurly = contents.get(i) instanceof LeftCurlyBrace;
			if (count == 0) {
				if (isRightCurly) {
					// found matching, return it
					return i;
				} else if (isLeftCurly) {
					count++;
				}
			} else {
				if (isRightCurly) {
					count --; // one less to go through
				} else if (isLeftCurly) {
					count ++;
				}
			}
		}
		return -1;
	}
	/**
	 * Gets the index of the closing parenthesis for the 'block'. 
	 * The starting point of the 'block' is the index after the opening (left) parenthesis.
	 */
	public static int getMatchingParen(int startBlock, ArrayList<Token> contents) {
		int count = 0;
		for (int i = startBlock; i < contents.size(); i++) {
			boolean isRightParen = contents.get(i) instanceof RightParentheses;
			boolean isLeftParen = contents.get(i) instanceof LeftParentheses;
			if (count == 0) {
				if (isRightParen) {
					// found matching, return it
					return i;
				} else if (isLeftParen) {
					count++;
				}
			} else {
				if (isRightParen) {
					count --; // one less to go through
				} else if (isLeftParen) {
					count ++;
				}
			}
		}
		return -1;
	}
}
