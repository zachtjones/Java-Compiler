package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class InterfaceNode implements Node {
	public boolean isPublic;
	public boolean isAbstract;
	public String name;
	public ArrayList<NameNode> supers; // extends stuff
	//TODO default methods and fields

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		for (NameNode n : supers) {
			n.resolveNames(c);
		}
	}   
}
