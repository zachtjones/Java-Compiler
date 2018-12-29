package x64.jni;

public enum JNIOffsets {
    FIND_CLASS(6), GET_OBJECT_CLASS(31), GET_METHOD_ID(33), GET_STATIC_FIELD_ID(144),

    GET_STATIC_OBJECT_FIELD(145), GET_STATIC_BOOLEAN_FIELD(146), GET_STATIC_BYTE_FIELD(147), GET_STATIC_CHAR_FIELD(148),
    GET_STATIC_SHORT_FIELD(149), GET_STATIC_INT_FIELD(150), GET_STATIC_LONG_FIELD(151), GET_STATIC_FLOAT_FIELD(152),
    GET_STATIC_DOUBLE_FIELD(153),

    CALL_VOID_METHOD(61), CALL_OBJECT_METHOD(34), CALL_BOOLEAN_METHOD(37), CALL_BYTE_METHOD(40), CALL_CHAR_METHOD(43),
    CALL_SHORT_METHOD(46), CALL_INT_METHOD(49), CALL_LONG_METHOD(52), CALL_FLOAT_METHOD(55), CALL_DOUBLE_METHOD(58),

    CALL_NON_VIRTUAL_VOID_METHOD(91), CALL_NON_VIRTUAL_OBJECT_METHOD(64), CALL_NON_VIRTUAL_BOOLEAN_METHOD(67),
    CALL_NON_VIRTUAL_BYTE_METHOD(70), CALL_NON_VIRTUAL_CHAR_METHOD(73), CALL_NON_VIRTUAL_SHORT_METHOD(76),
    CALL_NON_VIRTUAL_INT_METHOD(79), CALL_NON_VIRTUAL_LONG_METHOD(82), CALL_NON_VIRTUAL_FLOAT_METHOD(85),
    CALL_NON_VIRTUAL_DOUBLE_METHOD(88),

    NEW_STRING_UTF(167);

    /** Represents the index in the JNI function table */
    private final int index;
    JNIOffsets(int index) {
        this.index = index;
    }

    /**
     * Gets the offset for the GetStatic&lt;Type&gt;Field calls
     * @param fieldType The JNI type that is used to determine the offset
     * @return The integer offset into the virtual function table (the index * pointer size)
     */
    public static int getStaticFieldOffset(String fieldType) {
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

    /**
     * Gets the offset for the Call&lt;Type&gt;Method calls
     * @param nativeType The JNI type that is used to determine the offset
     * @return The integer offset into the virtual function table (the index * pointer size)
     */
    public static int getCallMethodOffset(String nativeType) {
        switch (nativeType) {
            case "V": return CALL_VOID_METHOD.getOffset();
            case "Z": return CALL_BOOLEAN_METHOD.getOffset();
            case "B": return CALL_BYTE_METHOD.getOffset();
            case "C": return CALL_CHAR_METHOD.getOffset();
            case "S": return CALL_SHORT_METHOD.getOffset();
            case "I": return CALL_INT_METHOD.getOffset();
            case "J": return CALL_LONG_METHOD.getOffset();
            case "F": return CALL_FLOAT_METHOD.getOffset();
            case "D": return CALL_DOUBLE_METHOD.getOffset();
        }
        return CALL_OBJECT_METHOD.getOffset();
    }

    /**
     * Gets the offset for the CallNonVirtual&lt;Type&gt;Method calls
     * @param nativeType The JNI type that is used to determine the offset
     * @return The integer offset into the virtual function table (the index * pointer size)
     */
    public static int getCallNonVirtualMethodOffset(String nativeType) {
        // indexes are all 30 higher * 8 bytes for pointer size
        return getCallMethodOffset(nativeType) + 30 * 8;
    }

    /** Returns the offset into the virtual function table for the JNI object for this method.
     * Use like: <code>mov OFFSET(JNI*), %temp; callq *%temp</code>*/
    public int getOffset() {
        return index * 8;
    }
}
