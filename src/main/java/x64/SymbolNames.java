package x64;

public class SymbolNames {

    public static String getFieldName(String javaClass, String javaField) {
        // use the JNI syntax
        return "_Java_" + escape(javaClass) + "_" + escape(javaField);
    }

    /** Escapes the name suitable for the name of the assembly / C function */
    private static String escape(String content) {
        return content
            .replace("_", "_1")
            .replace("<", "_5") // these 2 are defined by me to allow for calling constructors
            .replace(">", "_4") // ^^ same
            .replace("[", "_3") // the rest of these are defined by the JNI spec
            .replace(";", "_2")
            .replace('/', '_');
    }

    /** Returns the signature string used in JNI calls, created from the intermediate language type */
    public static String getJNISignatureFromILType(String typeFull) {

        // [Ljava/lang/Object; for Object[]
        // [[Ljava/lang/Object; for Object[][]

        // handle the primitives -- see below link for JNI docs
        // https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/types.html#primitive_types
        switch (typeFull) {
            case "boolean": return "Z";
            case "byte": return "B";
            case "char": return "C";
            case "short": return "S";
            case "int": return "I";
            case "long": return "J";
            case "float": return "F";
            case "double": return "D";
            case "void": return "V";
        }

        if (typeFull.contains("]")) {
            // remove a [] type
            final String oneLayerLess = typeFull.substring(0, typeFull.length() - 2);
            return "[" + getJNISignatureFromILType(oneLayerLess);
        } else {
            return "L" + typeFull + ";";
        }

        // TODO: mapping from primitives to their signatures
    }

    /***
     * Returns the label for the method name
     * @param className The class, as in java/lang/Object
     * @param name The name of the method, &lt;init&gt; for constructors
     * @return The label that is used to call to go to this method
     */
    public static String getMethodName(String className, String name) {
        // TODO include the args into the mangled signature
        return getFieldName(className, name);
    }
}
