package x64;

import static x64.allocation.CallingConvention.isMac;

public class SymbolNames {

	public static String getFieldName(String javaClass, String javaField) {
        // dependent on OS, mac os has leading underscores
        final String prefix = isMac ? "_Java_" : "Java_";

        return prefix + escape(javaClass) + "_" + escape(javaField);
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

    /***
     * Returns the label for the method name
     * @param className The class, as in java/lang/Object
     * @param name The name of the method, &lt;init&gt; for constructors
     * @return The label that is used to call to go to this method
     */
    public static String getMethodName(String className, String name) {
        return getFieldName(className, name);
        // TODO include the args into the mangled signature
    }
}
