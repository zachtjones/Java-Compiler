package x64.directives;

public class TypeDirective implements Directive {
	private String symbolName;

	public TypeDirective(String symbolName) {
		this.symbolName = symbolName;
	}

	@Override
	public String toString() {
		return ".type " + symbolName + ", @function";
	}
}
