package x64;

public class SymbolNames {

    public static String getMethodName(String javaClass, String javaMethod) {
        // use the JNI syntax
        return "_" + (javaClass + "." + javaMethod)
                .replace("_", "_1")
                .replace('/', '_');
    }
}
