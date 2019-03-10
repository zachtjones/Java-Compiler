package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/** Represents a method call. */
public class MethodCallNode extends NodeImpl implements Expression {
	@NotNull private final Expression object;
	@NotNull private final String methodName;
	@NotNull private final ArrayList<Expression> args;

    public MethodCallNode(@NotNull Expression object, @NotNull String methodName, @NotNull ArrayList<Expression> args,
                          @NotNull String fileName, int line) {

    	super(fileName, line);
		this.object = object;
		this.methodName = methodName;
		this.args = args;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
    	object.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {

		// compile in the arguments
		final Register[] argCompiled = new Register[args.size()];
		for (int i = 0; i < args.size(); i++) {
			args.get(i).compile(s, f);
			argCompiled[i] = f.allocator.getLast();
		}

    	// conditional logic based on whether the object is 'this', a class name, or an object calculation

		if (object instanceof ThisExpressionNode) {
			int tableValue = s.lookup(methodName);

			// could be static or instance field
			if (tableValue == SymbolTable.instanceFields) {

				// this get instance field
				object.compile(s, f);
				Register objectPointer = f.allocator.getLast();
				f.statements.add(new CallVirtualStatement(objectPointer, methodName, argCompiled,
					f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
			} else {

				// thisClass static method call
				final String className = f.parentClass;
				final Register result = f.allocator.getNext(Types.UNKNOWN);
				f.statements.add(new CallStaticStatement(className, methodName, argCompiled, result,
					getFileName(), getLine()));
			}

			// check if object is a Name and if the name represents a class name.
		} else if (object instanceof NameNode && s.lookup(((NameNode) object).primaryName) == SymbolTable.className) {
			// static method call
			final String className = ((NameNode)object).primaryName;
			final Register result = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new CallStaticStatement(className, methodName, argCompiled, result,
				getFileName(), getLine()));

		} else {
			// object method call
			object.compile(s, f);
			Register objectPointer = f.allocator.getLast();
			f.statements.add(new CallVirtualStatement(objectPointer, methodName, argCompiled,
				f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
		}
	}

}
