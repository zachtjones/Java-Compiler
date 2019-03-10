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

		// check if the parent is also a field access, and the parent's identifier is a class name
		if (object instanceof FieldExpressionNode) {
			String id = ((FieldExpressionNode)object).identifier;
			if (s.lookup(id) == SymbolTable.className) {
				// don't compile the object, just do the static field
				Register result = f.allocator.getNext(Types.UNKNOWN);
				f.statements.add(new GetStaticFieldStatement(id, identifier, result, getFileName(), getLine()));
				return;
			}
		}

		// conditional logic based on whether the object is 'this', a class name, or an object calculation
		if (object instanceof ThisExpressionNode) {
			int tableValue = s.lookup(identifier);

			// could be static or instance field
			if (tableValue == SymbolTable.instanceFields) {

				// this get instance field
				object.compile(s, f);
				Register objectPointer = f.allocator.getLast();
				f.statements.add(new GetInstanceFieldStatement(objectPointer, identifier,
					f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
			} else {

				// thisClass get static field
				final String className = f.parentClass;
				final Register result = f.allocator.getNext(Types.UNKNOWN);
				f.statements.add(new GetStaticFieldStatement(className, identifier, result, getFileName(), getLine()));
			}

			// check if object is a Name and if the name represents a class name.
		} else if (object instanceof NameNode && s.lookup(((NameNode) object).primaryName) == SymbolTable.className) {
			// static field access
			final String className = ((NameNode)object).primaryName;
			final Register result = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new GetStaticFieldStatement(className, identifier, result, getFileName(), getLine()));

		} else {
			// object get instance field
			object.compile(s, f);
			Register objectPointer = f.allocator.getLast();
			f.statements.add(new GetInstanceFieldStatement(objectPointer, identifier,
				f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
		}
	}

	@Override
	public void compileAddress(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {

		// conditional logic based on whether the object is 'this', a class name, or an object calculation

		// same as compile, but has Addresses instead of field values.
		if (object instanceof ThisExpressionNode) {
			int tableValue = s.lookup(identifier);

			// could be static or instance field
			if (tableValue == SymbolTable.instanceFields) {

				// this get instance field
				object.compile(s, f);
				Register objectPointer = f.allocator.getLast();
				f.statements.add(new GetInstanceFieldAddressStatement(objectPointer, identifier,
					f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
			} else {

				// thisClass get static field
				final String className = f.parentClass;
				final Register result = f.allocator.getNext(Types.UNKNOWN);
				f.statements.add(new GetStaticFieldAddressStatement(className, identifier, result,
					getFileName(), getLine()));
			}

			// check if object is a Name and if the name represents a class name.
		} else if (object instanceof NameNode && s.lookup(((NameNode) object).primaryName) == SymbolTable.className) {
			// static field access
			final String className = ((NameNode)object).primaryName;
			final Register result = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new GetStaticFieldAddressStatement(className, identifier, result, getFileName(), getLine()));

		} else {
			// object get instance field
			object.compile(s, f);
			Register objectPointer = f.allocator.getLast();
			f.statements.add(new GetInstanceFieldAddressStatement(objectPointer, identifier,
				f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
		}

	}
}
