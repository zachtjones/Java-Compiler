package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.LoadLiteralStatement;
import intermediate.Register;

/** ++ expr */
public class PreIncrementExpressionNode implements StatementExprNode, Expression {
    public Expression expr;
    public String fileName;
    public int line;
    
    public PreIncrementExpressionNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }
    
    @Override
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    public int getLine() {
    	return line;
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// get new one = expr
		// expr -- (but have to use the previous answer + 1, can't calculate 2x)
		// copy new new one from new one
		expr.compile(s, f);
		Register result = f.allocator.getLast();

		// add 1
		f.statements.add(new LoadLiteralStatement("1", f.allocator, fileName, line));
		Register one = f.allocator.getLast();

		f.statements.add(new BinaryOpStatement(result, one, f.allocator.getNext(result.type), '+', fileName, line));
		Register minusOne = f.allocator.getLast();
		// compile in the store to the address
		if (!(expr instanceof LValue)) {
			throw new CompileException("Can't assign the expression.", fileName, line);
		}
		((LValue)expr).compileAddress(s, f);
		// store it back
		f.statements.add(new CopyStatement(minusOne, f.allocator.getLast(), fileName, line));

		// result is before the addition
		f.statements.add(new CopyStatement(result, f.allocator.getNext(result.type), fileName, line));
	}
}
