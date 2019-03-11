package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.GetInstanceFieldAddressStatement;
import intermediate.GetInstanceFieldStatement;
import intermediate.GetStaticFieldAddressStatement;
import intermediate.GetStaticFieldStatement;
import intermediate.InterFunction;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

/** Represents accessing a field of an object. */
public class FieldExpressionNode extends NodeImpl implements Expression, LValue {
	@NotNull private final Expression object;
	@NotNull private String identifier;

    public FieldExpressionNode(@NotNull Expression object, @NotNull String identifier,
							   @NotNull String fileName, int line) {

    	super(fileName, line);
		this.object = object;
		this.identifier = identifier;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
    	object.resolveImports(c);
    	identifier = c.getFullName(identifier);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {

    	// compile the object
		object.compile(s, f);

		// check if it was a class name
		if (f.history.hasClassName()) {
			// static field access
			String name = f.history.getClassName();
			Register next = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new GetStaticFieldStatement(name, identifier, next, getFileName(), getLine()));
		} else {

			// instance field access
			Register object = f.allocator.getLast();
			Register next = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new GetInstanceFieldStatement(object, identifier, next, getFileName(), getLine()));
		}
	}

	@Override
	public void compileAddress(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {

		// compile the object
		object.compile(s, f);

		// check if it was a class name
		if (f.history.hasClassName()) {
			// static field access
			String name = f.history.getClassName();
			Register next = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new GetStaticFieldAddressStatement(name, identifier, next, getFileName(), getLine()));
		} else {

			// instance field access
			Register object = f.allocator.getLast();
			Register next = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new GetInstanceFieldAddressStatement(object, identifier, next, getFileName(), getLine()));
		}
	}
}
