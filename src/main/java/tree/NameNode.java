package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NameNode extends NodeImpl implements Expression, LValue {
	@NotNull public String primaryName;
	@Nullable private final ArrayList<GenericNode> generics;
	
    public NameNode(String fileName, int line, @NotNull String name, @Nullable ArrayList<GenericNode> generics) {
    	super(fileName, line);
    	this.primaryName = name;
    	this.generics = generics;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		primaryName = c.getFullName(primaryName);
		// don't have to check if one of primaryName or bounds is set, as this is handled by the parser
		
		// resolve the nested structures as well
		if (generics != null) {
			for (GenericNode n : generics) {
				n.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		if (generics != null) {
			throw new CompileException("Generics compiling not supported yet.", getFileName(), getLine());
			// TODO
		}

		if (primaryName.contains(".")) {
			throw new CompileException("Should be handled in the parser.", getFileName(), getLine());
		}

		// on the right side, get the result
		int tableLookup = s.lookup(primaryName);

		if (tableLookup == -1) {
			throw new CompileException("symbol: '" + primaryName + "' not found.",
				getFileName(), getLine());
		}

		final Types type = s.getType(primaryName);

		if (tableLookup == SymbolTable.local) {
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetLocalStatement(result, primaryName, getFileName(), getLine()));
		} else if (tableLookup == SymbolTable.parameter) {
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetParamStatement(result, primaryName, getFileName(), getLine()));
		} else if (tableLookup == SymbolTable.staticFields) {
			// static field
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetStaticFieldStatement(f.parentClass, primaryName, result, getFileName(), getLine()));
		} else if (tableLookup == SymbolTable.instanceFields) {
			// load 'this' pointer
			Register thisPointer = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new GetParamStatement(thisPointer, "this", getFileName(), getLine()));

			// load the field from 'this' pointer
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetInstanceFieldStatement(thisPointer, primaryName, result,
				getFileName(), getLine()));

			f.history.setName(primaryName);
		}
	}

	@Override
	public void compileAddress(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {

		if (generics != null) {
			throw new CompileException("Generics compiling not supported yet.", getFileName(), getLine());
			// TODO
		}

		if (primaryName.contains(".")) {
			throw new CompileException("Should be handled in the parser.", getFileName(), getLine());
		}

		f.history.setName(primaryName);
		// on the left side, get the address

		int tableLookup = s.lookup(primaryName);
		if (tableLookup == -1) {
			throw new CompileException("symbol: '" + primaryName + "' not defined.",
				getFileName(), getLine());
		}
		final Types type = s.getType(primaryName);

		if (tableLookup == SymbolTable.local) {
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetLocalAddressStatement(result, primaryName, getFileName(), getLine()));
		} else if (tableLookup == SymbolTable.parameter) {
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetParamAddressStatement(result, primaryName, getFileName(), getLine()));
		} else if (tableLookup == SymbolTable.staticFields) {
			// static field
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetStaticFieldAddressStatement(f.parentClass, primaryName, result, getFileName(), getLine()));
		} else if (tableLookup == SymbolTable.instanceFields) {
			// instance field: load 'this' pointer
			Register thisPointer = f.allocator.getNext(Types.UNKNOWN);
			f.statements.add(new GetParamStatement(thisPointer, "this", getFileName(), getLine()));

			// load the field from 'this' pointer
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetInstanceFieldAddressStatement(thisPointer, primaryName, result,
				getFileName(), getLine()));
		} else {
			throw new CompileException("unsure what to do here", "NameNode.java", 152);
		}
	}
}
