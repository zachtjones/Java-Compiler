package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.CallStaticStatement;
import intermediate.CallVirtualStatement;
import intermediate.InterFunction;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** ( expressions * )
* This is the second part of a function call, the arguments list. */
public class ArgumentExpressionNode extends NodeImpl implements Expression {
    /** The expressions to evaluate before the function call. */
    @NotNull private final ArrayList<Expression> expressions;

    public ArgumentExpressionNode(@NotNull String fileName, int line, @Nullable ArrayList<Expression> expressions) {
    	super(fileName, line);
    	this.expressions = expressions == null ? new ArrayList<>() : expressions;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		for (Expression e : expressions) {
			e.resolveImports(c);
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {

		// remove the getInstanceField part always added in from NameNode (in case it's a field)
		f.statements.remove(f.statements.size() - 1);

		// have to go back 2 registers if it's a non-static call, need to obtain before adding args
		Register obj = f.allocator.get2Before();

		final String className = f.history.getTwoNamesAgo();
		final String functionName = f.history.getName();

		// compile in the args
		Register[] args = new Register[expressions.size()];
		for (int i = 0; i < expressions.size(); i++) {
			expressions.get(i).compile(s, f);
			args[i] = f.allocator.getLast();
		}

		// determine if static function call or instance function call
		final String name = className != null ? className : functionName;

		if (Types.CLASS.equals(s.getType(name)) && className != null) {
			// static function call
			f.statements.add(new CallStaticStatement(className, functionName, args,
				f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
		} else {

			// add in the call virtual statement
			f.statements.add(new CallVirtualStatement(obj, functionName, args,
				f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
		}
	}

	@NotNull
	ArrayList<Expression> getExpressions() {
		return expressions;
	}
}
