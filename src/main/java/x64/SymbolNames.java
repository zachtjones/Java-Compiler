package x64;

public class SymbolNames {

    public static String getFieldName(String javaClass, String javaField) {
        // use the JNI syntax
        return "_" + escape(javaClass) + "_" + escape(javaField);
    }

    private static String escape(String content) {
        return content.replace("_", "_1")
                .replace('/', '_');
    }

    /** Returns the signature string used in JNI calls, created from the intermediate language type */
    public static String getJNISignatureFromILType(String typeFull) {

        // [Ljava/lang/Object; for Object[]
        // [L[Ljava/lang/Object;; for Object[][]

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
        }

        if (typeFull.contains("[")) {
            // remove a [] type
            final String oneLayerLess = typeFull.substring(0, typeFull.length() - 2);
            return "[L" + getJNISignatureFromILType(oneLayerLess) + ";";
        } else {
            return typeFull;
        }

        // TODO: mapping from primitives to their signatures
    }
}
