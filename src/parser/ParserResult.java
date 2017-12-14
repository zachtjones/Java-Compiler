package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import tokens.ClassToken;
import tokens.Token;

public class ParserResult {
	public ArrayList<Token> contents = new ArrayList<Token>();
	public String packageId = ""; // example java.io
	public HashSet<String> imports = new HashSet<String>(); // example: java.util.*, java.io.File
	
	// also includes interfaces, enum
	public HashSet<ClassToken> classesUsed = new HashSet<ClassToken>();
	public HashMap<ClassToken, ClassToken> inheritances = new HashMap<ClassToken, ClassToken>(); // child -> parent class
	// also interface -> classes that implement it
	public HashMap<ClassToken, ArrayList<ClassToken>> implementations = new HashMap<ClassToken, ArrayList<ClassToken>>();
}
