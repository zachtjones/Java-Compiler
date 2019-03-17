package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/** Represents a method call. */
public class MethodCallNode extends NodeImpl implements Expression {
	@Nullable private final Expression object;
	@NotNull private final String methodName;
	@NotNull private final ArrayList<Expression> args;

	/**
	 * Creates a method call. This method call could be static, virtual, or non-virtual. (given the context of object)
	 * @param object The Expression that results in something to qualify the method as static or instance.
	 *               If null, represents either a static/instance call to something in this class.
	 * @param methodName The name of the method
	 * @param args The list of expressions that represent the arguments.
	 * @param fileName The filename being parsed.
	 * @param line The current line number.
	 */
    public MethodCallNode(@Nullable Expression object, @NotNull String methodName, @NotNull ArrayList<Expression> args,
						  @NotNull String fileName, int line) {

    	super(fileName, line);
		this.object = object;
		this.methodName = methodName;
		this.args = args;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
    	if (object != null) object.resolveImports(c);

    	for (Expression e : args) {
    		e.resolveImports(c);
		}
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

		if (object != null) {
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
		} else { // method in this file

			// object is null, use the symbol table lookup to determine what it is
			if (s.getType(methodName).equals(Types.INSTANCE_FUNCTION)) {

				// this.methodName(args)
				Register thisPointer = f.allocator.getNext(Types.UNKNOWN);
				f.addStatement(new GetLocalStatement(thisPointer, "this", getFileName(), getLine()));

				f.addStatement(new CallVirtualStatement(thisPointer, methodName, argCompiled,
					f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));

			} else if (s.getType(methodName).equals(Types.STATIC_FUNCTION)) {
				// ThisClass.methodName(args)
				String thisClassName = f.parentClass;
				f.addStatement(new CallStaticStatement(thisClassName, methodName, argCompiled,
					f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
			} else {
				throw new CompileException("Method: " + methodName + " in this file does not exist.",
					getFileName(), getLine());
			}
		}
	}

}
