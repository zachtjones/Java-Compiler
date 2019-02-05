package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.CreateArrayStatement;
import intermediate.InterFunction;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

/** new type[expression or empty] ... */
public class PrimitiveArrayAllocationNode extends NodeImpl implements Expression {
    public Types type;
    public ArrayList<Expression> expressions; // never empty list, but can have null in the list
    
    public PrimitiveArrayAllocationNode(String fileName, int line) {
    	super(fileName, line);
    }

    @Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		for (Expression e : expressions) {
			if (e != null) {
				e.resolveImports(c);
			}
		}
	}
    
	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		if (expressions.get(0) == null) {
			throw new CompileException(
				"The first dimension of an array has to have a size given at the constructor.",
				getFileName(), getLine());
		}
		if (expressions.size() == 1) {
			expressions.get(0).compile(s, f);
			Register size = f.allocator.getLast();
			Register result = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new CreateArrayStatement(size, type, result));
		} else {
			// TODO handle multi-dimensional arrays.
			throw new CompileException("Multi-dimensional array creation not done yet.", "", -1);
		}
	}
}
