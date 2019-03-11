package intermediate;

import java.util.ArrayList;
import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import tree.CompileHistory;
import x64.X64Context;
import x64.X64File;

public class InterFunction {

	@NotNull public final String parentClass;
	@NotNull public final String name;
	@NotNull public final Types returnType;
	public boolean isInstance; // instance or static
	public boolean isInit; // static init or instance init

	@NotNull public ArrayList<Types> paramTypes;
	@NotNull public ArrayList<String> paramNames;
	public boolean lastArgVarargs;

	@NotNull public final ArrayList<String> throwsList;

	@NotNull private final ArrayList<InterStatement> statements;

	@NotNull public final RegisterAllocator allocator;
	@NotNull public final CompileHistory history;


	
	public InterFunction(@NotNull String fromClass, @NotNull String name, @NotNull Types returnType) {
		parentClass = fromClass;
		this.name = name;
		this.returnType = returnType;

		this.paramTypes = new ArrayList<>();
		this.paramNames = new ArrayList<>();
		this.throwsList = new ArrayList<>();
		this.statements = new ArrayList<>();

		allocator = new RegisterAllocator();
		history = new CompileHistory();
	}

	/**
	 * Adds a intemediate language statement to the list of statements.
	 * @param statement The statement to add.
	 */
	public void addStatement(InterStatement statement) {
		statements.add(statement);
	}

	/** Returns true if and only if this method is a constructor */
	public boolean isConstructor() {
		return name.equals("<init>");
	}
	
	@Override
	public String toString() {
		// declaration
		StringBuilder sb = new StringBuilder("function ");
		if (isInstance) {
			sb.append("instance ");
		} else {
			sb.append("static ");
		}
		if (lastArgVarargs) {
			sb.append("varargs ");
		}
		if (isInit) {
			sb.append("init\n");
		} else {
			sb.append(name);
			// parameters
			sb.append(" (");
			for (int i = 0; i < paramTypes.size(); i++) {
				sb.append(paramNames.get(i));
				sb.append('<');
				sb.append(paramTypes.get(i));
				sb.append('>');
				sb.append(' ');
			}
			sb.append(')');

			sb.append(" returns ");
			sb.append(returnType);

			// throws list
			if (this.throwsList.size() != 0) {
				sb.append(" throws(");
				for (String s : throwsList) {
					sb.append(s);
					sb.append(' ');
				}
				sb.append(')');
			}
			sb.append('\n');
		}
		
		// body
		for (InterStatement s : statements) {
			sb.append('\t');
			sb.append(s.toString());
			sb.append('\n');
		}
		
		sb.append("end function\n");
		
		return sb.toString();
	}

	/** Type checks all the statements. 
	 * @param className	The fully qualified name for the InterFile. 
	 * @throws CompileException If there is an error with type checking.*/
	public void typeCheck(Types className) throws CompileException {
		HashMap<Register, Types> definitions = new HashMap<>();
		HashMap<String, Types> locals = new HashMap<>();
		// define parameters and fill it in.
		HashMap<String, Types> params = new HashMap<>();
		for (int i = 0; i < paramTypes.size(); i++) {
			params.put(paramNames.get(i), paramTypes.get(i));
		}
		// instance functions have access to 'this'
		if (this.isInstance) {
			params.put("this", className);
		}
		
		for (InterStatement i : statements) {
			i.typeCheck(definitions, locals, params, this);
		}		
	}

	/**
	 * Compiles down to the assembly.
	 * Note the x64 assembly has unlimited registers until it goes to the next step.
	 * @param assemblyFile The assembly file to add instructions to.
	 */
    public void compile(X64File assemblyFile) throws CompileException {

        // TODO the name mangled args to allow for method overloading
		X64Context context = new X64Context(assemblyFile, allocator, name);

        // add the instructions for the statements -> x64
        for (InterStatement statement : statements) {
            statement.compile(context);
        }

        context.addFunctionToFile();
    }
}