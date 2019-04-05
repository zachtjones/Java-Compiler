package x64.directives;

/** Assembles the string into a char array into the object file. The char array is terminated with \0 character. */
public class AsciizString extends Directive {

    public AsciizString(String content) {
       super(String.format(".asciz \"%s\"", content));
    }
}
