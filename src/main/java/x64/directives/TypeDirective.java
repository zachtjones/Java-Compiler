package x64.directives;

/** Represents a debugging info declaring the label as a function. */
public class TypeDirective extends Directive {

	public TypeDirective(String symbolName) {
		super(".type " + symbolName + ", @function");
	}
}
