package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.*;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a single identifier, which is used solely as the entire primary expression.
 */
public class UnknownIdentifierNode extends NodeImpl implements Expression, LValue {

    @NotNull private String identifier;

    public UnknownIdentifierNode(@NotNull String identifier, @NotNull String fileName, int line) {
        super(fileName, line);
        this.identifier = identifier;
    }

    @Override
    public void resolveImports(@NotNull ClassLookup c) throws CompileException {
        identifier = c.getFullName(identifier);
    }

    @Override
    public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
        // could be either: local, class name, or field of this. If this is class name, it's an error
        int tableLookup = s.lookup(identifier);
        Register result = f.allocator.getNext(Types.UNKNOWN);

        f.history.clearClassName();

        if (tableLookup == SymbolTable.local) {
            // get local statement
            f.statements.add(new GetLocalStatement(result, identifier, getFileName(), getLine()));

        } else if (tableLookup == SymbolTable.parameter) {
            // get param statement
            f.statements.add(new GetParamStatement(result, identifier, getFileName(), getLine()));

        } else if (tableLookup == SymbolTable.staticFields) {
            // get static field of this class
            f.statements.add(new GetStaticFieldStatement(f.parentClass, identifier, result, getFileName(), getLine()));

        } else if (tableLookup == SymbolTable.instanceFields) {
            // get instance field of this class
            f.statements.add(new GetParamStatement(result, "this", getFileName(), getLine()));
            Register field = f.allocator.getNext(Types.UNKNOWN);
            f.statements.add(new GetInstanceFieldStatement(result, identifier, field, getFileName(), getLine()));

        } else if (tableLookup == SymbolTable.className) {
            // mark the past as a class use
            f.history.setClassName(identifier);

        } else {
            throw new CompileException("Error: " + identifier + " is not an expression.", getFileName(), getLine());
        }
    }

    @Override
    public void compileAddress(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
        // could be either: local, class name, or field of this. If this is class name, it's an error
        int tableLookup = s.lookup(identifier);
        Register result = f.allocator.getNext(Types.UNKNOWN);

        if (tableLookup == SymbolTable.local) {
            // get local statement
            f.statements.add(new GetLocalAddressStatement(result, identifier, getFileName(), getLine()));

        } else if (tableLookup == SymbolTable.parameter) {
            // get param statement
            f.statements.add(new GetParamAddressStatement(result, identifier, getFileName(), getLine()));

        } else if (tableLookup == SymbolTable.staticFields) {
            // get static field of this class
            f.statements.add(new GetStaticFieldAddressStatement(f.parentClass, identifier, result,
                getFileName(), getLine()));

        } else if (tableLookup == SymbolTable.instanceFields) {
            // get instance field of this class
            f.statements.add(new GetParamStatement(result, "this", getFileName(), getLine()));
            Register field = f.allocator.getNext(Types.UNKNOWN);
            f.statements.add(new GetInstanceFieldAddressStatement(result, identifier, field,
                getFileName(), getLine()));

        } else if (tableLookup == SymbolTable.className) {
            // mark the past as a class use
            f.history.setClassName(identifier);

        } else {
            throw new CompileException("Error: " + identifier + " is not an expression.", getFileName(), getLine());
        }
    }
}
