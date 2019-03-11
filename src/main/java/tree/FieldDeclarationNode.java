package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.*;
import org.jetbrains.annotations.NotNull;

public class FieldDeclarationNode extends NodeImpl {
	public boolean isPublic;
	public boolean isProtected;
	public boolean isPrivate;
	public boolean isStatic;
	public boolean isFinal;
	public boolean isTransient;
	public boolean isVolatile;

	public Types type;
	public ArrayList<VariableDecNode> variables = new ArrayList<>();

    public FieldDeclarationNode(String fileName, int line) {
    	super(fileName, line);
    }

	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		type = type.resolveImports(c, getFileName(), getLine());
	}

	/**
	 * Compiles this into the Intermediate file
	 * @param f the intermediate file to compile into.
	 * @param syms The symbol table entry to put the field declaration into.
	 * @throws CompileException if there is a compiling exception thrown.
	 */
	public void compile(InterFile f, SymbolTable syms) throws CompileException {
		// add the type declarations to the instance structure, and the
		//  initial values to the instance init
		for (VariableDecNode d : variables) {
			// fix: String[] id[] -> String[][] id;
			Types temp = type;
			for (int i = 0; i < d.id.numDimensions; i++) {
				temp = Types.arrayOf(temp);
			}
			f.addField(temp, d.id.name, isStatic);

			// add the initial values if any
			if (d.init != null && d.init.e != null) {
				// add instance initializer
				final String name = isStatic ? "<clinit>" : "<init>"; // following java .class standard here
				InterFunction func = new InterFunction(f.getName(), name, Types.VOID);
				func.isInit = true;
				func.isInstance = !isStatic;

				// compile in the expression
				d.init.e.compile(syms, func);
				Register value = func.allocator.getLast();
				Register pointer = func.allocator.getNext(Types.UNKNOWN);

				// store in the field
				if (isStatic) {
					// save value back to the static field
					func.addStatement(
						new GetStaticFieldAddressStatement(f.getName(), d.id.name, pointer, getFileName(), getLine()));
				} else {
					// save value back to the instance field of this
					Register thisPointer = func.allocator.getNext(Types.UNKNOWN);
					func.addStatement(
						new GetParamStatement(thisPointer, "this", getFileName(), getLine())
					);
					func.addStatement(
						new GetInstanceFieldAddressStatement(thisPointer, d.id.name, pointer, getFileName(), getLine())
					);

				}
				// store at the pointer
				func.addStatement(
					new StoreAddressStatement(value, pointer, getFileName(), getLine())
				);

				f.addFunction(func);
			}
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		/* Nothing to do here*/
	}

	/** places the symbols declared by this field declaration */
	void putSymbols(SymbolTable staticFields, SymbolTable instanceFields) throws CompileException {
		// choose which one to place them in
		SymbolTable choice = isStatic ? staticFields : instanceFields;
		for (VariableDecNode variable : variables) {
			choice.putEntry(variable.id.name, type, getFileName(), getLine());
		}
	}
}
