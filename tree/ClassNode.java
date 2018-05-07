package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

/** This represents a class in the tree
* @author Zach Jones
*/
public class ClassNode implements Node {
    public boolean isAbstract;
    public boolean isFinal;
    public boolean isPublic;
    public String name;
    public NameNode superclass;
    public ArrayList<NameNode> interfaces;
    public ArrayList<ClassBodyNode> body = new ArrayList<>();
    public ArrayList<NameNode> typeParams;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		if (superclass != null) {
			superclass.resolveNames(c);
		}
		if (interfaces != null) {
			for (NameNode n : interfaces) {
				n.resolveNames(c);
			}	
		}
		for (ClassBodyNode b : body) {
			b.resolveNames(c);
		}
		if (typeParams != null) {
			for (NameNode n : typeParams) {
				n.resolveNames(c);
			}
		}
		
	}
}
