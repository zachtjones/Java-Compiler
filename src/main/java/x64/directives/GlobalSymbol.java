package x64.directives;

/** Represents the .global NAME directive, useful for imports and exports. */
public class GlobalSymbol extends Directive {

    public GlobalSymbol(String name) {
        super(".global " + name);
    }
}
