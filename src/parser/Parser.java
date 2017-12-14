package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

import tokens.ClassToken;
import tokens.KeywordToken;
import tokens.KeywordType;
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
		// 3rd pass, convert ClassTokens to classes
		groupTokens(result);
		
		return result;
	}
	
	private static ParserResult generateTokens(String file) throws IOException {
		String contents = new String(Files.readAllBytes(Paths.get(file)));
		contents = contents.replaceAll("//.*?\n", "\n"); //replace line comments
		contents = contents.replaceAll("/\\*[\\s\\S]*?\\*/", ""); // replace block comments
		StringTokenizer st = new StringTokenizer(contents, " \t\r\n\f\"[]{}();.", true);
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
				// generic Token for now
				result.contents.add(new Symbol(token));
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
		for (Token t : result.contents) {
			if (t instanceof Symbol) {
				Symbol s = (Symbol)t;
				if (ClassToken.matches(s.contents)) {
					classSeq.add(new ClassToken(s.contents));
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

	private static void groupTokens(ParserResult result) {
		ArrayList<Token> contents = result.contents;
		for (int i = 0; i < contents.size(); i++) {
			Token t = contents.get(i);
			if (t instanceof KeywordToken) {
				KeywordToken k = (KeywordToken) t;
				if (k.t == KeywordType.CLASS) {
					// TODO determine the class's declaration and the block of it
					// extends and implements
				} else if (k.t == KeywordType.INTERFACE) {
					
				}
			}
		}
	}
}
