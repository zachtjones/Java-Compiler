package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class EnumNode implements Node {
    public String name;
    public ArrayList<String> values;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// nothing needed
	}
}
