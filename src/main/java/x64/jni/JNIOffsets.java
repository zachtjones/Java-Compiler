package x64.jni;

public enum JNIOffsets {
    FIND_CLASS(6), GET_OBJECT_CLASS(31), GET_METHOD_ID(33), GET_STATIC_FIELD_ID(144),
    GET_STATIC_OBJECT_FIELD(145), GET_STATIC_BOOLEAN_FIELD(146), GET_STATIC_BYTE_FIELD(147), GET_STATIC_CHAR_FIELD(148),
    GET_STATIC_SHORT_FIELD(149), GET_STATIC_INT_FIELD(150), GET_STATIC_LONG_FIELD(151), GET_STATIC_FLOAT_FIELD(152),
    GET_STATIC_DOUBLE_FIELD(153);

    /** Represents the index in the JNI function table */
    private final int index;
    JNIOffsets(int index) {
        this.index = index;
    }

    public static int getStaticFieldOffset(String fieldType) {
        // the primitives are relative to the object type, can create a helper method for the index
        //  when we add more constants for SET_STATIC_<TYPE>_FIELD or other similar families.
        switch (fieldType) {
            case "Z": return GET_STATIC_BOOLEAN_FIELD.getOffset();
            case "B": return GET_STATIC_BYTE_FIELD.getOffset();
            case "C": return GET_STATIC_CHAR_FIELD.getOffset();
            case "S": return GET_STATIC_SHORT_FIELD.getOffset();
            case "I": return GET_STATIC_INT_FIELD.getOffset();
            case "J": return GET_STATIC_LONG_FIELD.getOffset();
            case "F": return GET_STATIC_FLOAT_FIELD.getOffset();
            case "D": return GET_STATIC_DOUBLE_FIELD.getOffset();
        }
        return GET_STATIC_OBJECT_FIELD.getOffset();
    }

    /** Returns the offset into the virtual function table for the JNI object for this method.
     * Use like: <code>mov OFFSET(JNI*), %temp; callq *%temp</code>*/
    public int getOffset() {
        return index * 8;
    }
}
