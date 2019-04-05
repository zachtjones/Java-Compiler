package x64.directives;

/** Assembles some unallocated space, which cuts down on object file size. */
public class SpaceDirective extends Directive {

	public SpaceDirective(int size) {
		super(".space " + size);
	}
}
