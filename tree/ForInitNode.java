package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class ForInitNode implements Node {
	// either this, or the second
	public LocalVariableDecNode dec;
	// or this:
	public ArrayList<StatementExprNode> items;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		if (dec != null) dec.resolveNames(c);
		else {
			for (StatementExprNode s : items) {
				s.resolveNames(c);
			}
		}
	}
}
