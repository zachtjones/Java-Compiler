package x64.directives;

public class TypeDirective extends Directive {
	private String symbolName;

	public TypeDirective(String symbolName) {
		this.symbolName = symbolName;
	}

	@Override
	public String assemblyRepresentation() {
		return ".type " + symbolName + ", @function";
	}
}
