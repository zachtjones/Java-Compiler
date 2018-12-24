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
}
