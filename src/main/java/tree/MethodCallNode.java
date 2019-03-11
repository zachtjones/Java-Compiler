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

		// compile the object/class/whatever field chain that could be
		object.compile(s, f);

    	// determine if the last thing compiled was just a class name
		if (f.history.hasClassName()) {
			// static function call
			String className = f.history.getClassName();
			f.addStatement(new CallStaticStatement(className, methodName, argCompiled,
				f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));

		} else {
			// instance function call of the last register allocated
			Register objectPointer = f.allocator.getLast();
			f.addStatement(new CallVirtualStatement(objectPointer, methodName, argCompiled,
				f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
		}
	}

}
