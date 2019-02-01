package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class PrimaryExpressionNode extends NodeImpl implements Expression, LValue {
    public Expression prefix;
    public ArrayList<Expression> suffixes = new ArrayList<>();
    
    public PrimaryExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		prefix.resolveImports(c);
		for (Expression e : suffixes) {
			e.resolveImports(c);
		}
	}
	
	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		prefix.compile(s, f);
		for (Expression e : suffixes) {
			e.compile(s, f);
		}
		
	}

	@Override
	public void compileAddress(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// compile the address of the last part
		prefix.compile(s, f);
		for (int i = 0; i < suffixes.size(); i++) {
			if (i == suffixes.size() - 1) { // last one
				if (suffixes.get(i) instanceof LValue) {
					( (LValue) suffixes.get(i) ).compileAddress(s, f);
				} else {
					throw new CompileException("left side of = expression not able to assign to. " 
						+ suffixes.get(i), getFileName(), getLine());
				}
			} else {
				suffixes.get(i).compile(s, f);
			}
		}
	}
}
