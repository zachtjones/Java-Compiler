package tree;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import intermediate.EndScopeStatement;
import intermediate.InterFunction;
import intermediate.LabelStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an entry in the symbol table, with a reference to the outer
 * scope.
 * @author Zach Jones
 *
 */
public class SymbolTable {

	/** Reference to an outer scope */
	private final SymbolTable outer;
	/** The scope level of this symbol table */
	private final int scopeLevel;

	/** label to jump to with a break; May be null */
	@Nullable private LabelStatement breakLabel = null;
	@Nullable private LabelStatement continueLabel = null;
	
	// places where the symbol is defined -- valid values of scope level
	public static final int className = 0;
	public static final int parameter = 1;
	public static final int local = 2;
	static final int staticFields = 3;
	static final int instanceFields = 4;
	
	// hash map of identifier to the type
	private HashMap<String, Types> entries;
	
	public SymbolTable(SymbolTable outer, int scopeLevel) {
		this.outer = outer;
		this.scopeLevel = scopeLevel;
		this.entries = new HashMap<>();
	}
	
	/**
	 * Looks up the identifier in the current symbol table, or further up scope.
	 * @param identifier The identifier to find
	 * @return The scope of the identifier found, or -1 if not found.
	 */
	int lookup(String identifier) {
		int result = lookupThisScope(identifier);
		if (result == -1 && outer != null) {
			result = outer.lookup(identifier);
		}
		return result;
	}
	
	/**
	 * Looks up the identifier in the current symbol table only.
	 * @param identifier The identifier to find
	 * @return The scope of the identifier found, or -1 if not found
	 */
	public int lookupThisScope(String identifier) {
		if (entries.containsKey(identifier)) {
			return scopeLevel;
		} else {
			return -1;
		}
	}
	
	/**
	 * Gets the type of the identifier, or null if not found.
	 * @param identifier The identifier to find the type of.
	 * @return A string that is the intermediate file's representation of the type.
	 */
	Types getType(String identifier) {
		Types result = entries.get(identifier);
		if (result == null && outer != null) {
			result = outer.getType(identifier);
		}
		return result;
	}
	
	/**
	 * Places the entry into this symbol table
	 * @param identifier The identifier to place
	 * @param type The identifier's type.
	 * @param fileName The file name of the currently compiled expression
	 * @param line The line number of the currently compiled expression
	 * @throws CompileException If the symbol is already defined
	 */
	public void putEntry(String identifier, Types type, String fileName, int line)
			throws CompileException {
		
		// can't have two variables with same name in same scope
		if (lookup(identifier) == scopeLevel) {
			throw new CompileException("duplicate variable, '" + identifier
					+ "' reference in same scope", fileName, line);
		}
				
		// also can't have parameter declared for local
		if (scopeLevel == local && lookup(identifier) == parameter) {
			throw new CompileException("variable, '" + identifier + "' is also a parameter",
					fileName, line);
		}
		
		// good, the variable can be defined here
		entries.put(identifier, type);
	}

	/***
	 * Inserts the code for ending the scope of all variables in this scope into the function.
	 * @param function The function to add the end scope statements.
	 */
	void endScope(InterFunction function) {
		for (String local : entries.keySet()) {
			function.addStatement(new EndScopeStatement(local));
		}
	}

	/**
	 * Gets the label associated with the break statement, throwing an exception if not found.
	 * @param fileName The filename being currently compiled.
	 * @param line The line number in that file.
	 * @return The Label that break should take the jump to.
	 * @throws CompileException
	 */
	LabelStatement getBreakLabel(@NotNull String fileName, int line) throws CompileException {
		if (breakLabel != null) return breakLabel;

		if (outer != null) return outer.getBreakLabel(fileName, line);

		throw new CompileException("break is not allowed here.", fileName, line);
	}

	/**
	 * Gets the label associated with the continue statement, throwing an exception if not found.
	 * @param fileName The filename being currently compiled.
	 * @param line The line number in that file.
	 * @return The Label that continue should take the jump to.
	 * @throws CompileException
	 */
	LabelStatement getContinueLabel(@NotNull String fileName, int line) throws CompileException {
		if (continueLabel != null) return continueLabel;

		if (outer != null) return outer.getContinueLabel(fileName, line);

		throw new CompileException("continue is not allowed here.", fileName, line);
	}

	/** Sets the label to go to on 'break'*/
	void setBreakLabel(@NotNull LabelStatement label) {
		breakLabel = label;
	}

	/** Sets the label to go to on 'continue' */
	void setContinueLabel(@NotNull LabelStatement label) {
		continueLabel = label;
	}
}
