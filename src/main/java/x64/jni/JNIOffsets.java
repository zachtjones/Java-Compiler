package x64.jni;

import helper.Types;

public enum JNIOffsets {
    FIND_CLASS(6), ALLOC_OBJECT(27), GET_OBJECT_CLASS(31), GET_METHOD_ID(33),

    CALL_OBJECT_METHOD(34), CALL_BOOLEAN_METHOD(37), CALL_BYTE_METHOD(40),
    CALL_CHAR_METHOD(43), CALL_SHORT_METHOD(46), CALL_INT_METHOD(49), CALL_LONG_METHOD(52),
    CALL_FLOAT_METHOD(55), CALL_DOUBLE_METHOD(58), CALL_VOID_METHOD(61),

    CALL_NON_VIRTUAL_OBJECT_METHOD(64), CALL_NON_VIRTUAL_BOOLEAN_METHOD(67),
    CALL_NON_VIRTUAL_BYTE_METHOD(70), CALL_NON_VIRTUAL_CHAR_METHOD(73),
    CALL_NON_VIRTUAL_SHORT_METHOD(76), CALL_NON_VIRTUAL_INT_METHOD(79),
    CALL_NON_VIRTUAL_LONG_METHOD(82), CALL_NON_VIRTUAL_FLOAT_METHOD(85),
    CALL_NON_VIRTUAL_DOUBLE_METHOD(88), CALL_NON_VIRTUAL_VOID_METHOD(91),

    GET_INSTANCE_FIELD_ID(94),

    GET_INSTANCE_OBJECT_FIELD(95), GET_INSTANCE_BOOLEAN_FIELD(96), GET_INSTANCE_BYTE_FIELD(97),
    GET_INSTANCE_CHAR_FIELD(98), GET_INSTANCE_SHORT_FIELD(99), GET_INSTANCE_INT_FIELD(100),
    GET_INSTANCE_LONG_FIELD(101), GET_INSTANCE_FLOAT_FIELD(102), GET_INSTANCE_DOUBLE_FIELD(103),

    SET_INSTANCE_OBJECT_FIELD(104), SET_INSTANCE_BOOLEAN_FIELD(105), SET_INSTANCE_BYTE_FIELD(106),
    SET_INSTANCE_CHAR_FIELD(107), SET_INSTANCE_SHORT_FIELD(108), SET_INSTANCE_INT_FIELD(109),
    SET_INSTANCE_LONG_FIELD(110), SET_INSTANCE_FLOAT_FIELD(111), SET_INSTANCE_DOUBLE_FIELD(112),

    GET_STATIC_METHOD_ID(113),

    CALL_STATIC_OBJECT_METHOD(114), CALL_STATIC_BOOLEAN_METHOD(117), CALL_STATIC_BYTE_METHOD(120),
    CALL_STATIC_CHAR_METHOD(123), CALL_STATIC_SHORT_METHOD(126), CALL_STATIC_INT_METHOD(129),
    CALL_STATIC_LONG_METHOD(132), CALL_STATIC_FLOAT_METHOD(135), CALL_STATIC_DOUBLE_METHOD(138),
    CALL_STATIC_VOID_METHOD(141),

    GET_STATIC_FIELD_ID(144),

    GET_STATIC_OBJECT_FIELD(145), GET_STATIC_BOOLEAN_FIELD(146), GET_STATIC_BYTE_FIELD(147),
    GET_STATIC_CHAR_FIELD(148), GET_STATIC_SHORT_FIELD(149), GET_STATIC_INT_FIELD(150),
    GET_STATIC_LONG_FIELD(151), GET_STATIC_FLOAT_FIELD(152), GET_STATIC_DOUBLE_FIELD(153),

    SET_STATIC_OBJECT_FIELD(154), SET_STATIC_BOOLEAN_FIELD(155), SET_STATIC_BYTE_FIELD(156),
    SET_STATIC_CHAR_FIELD(157), SET_STATIC_SHORT_FIELD(158), SET_STATIC_INT_FIELD(159),
    SET_STATIC_LONG_FIELD(160), SET_STATIC_FLOAT_FIELD(161), SET_STATIC_DOUBLE_FIELD(162),

    NEW_STRING_UTF(167);

    /** Represents the index in the JNI function table */
    private final int index;
    JNIOffsets(int index) {
        this.index = index;
    }

    /** Returns the offset into the virtual function table for the JNI object for this method.
     * Use like: <code>mov OFFSET(JNI*), %temp; callq *%temp</code>*/
    public int getOffset() {
        return index * 8;
    }

    /**
     * Since all JNI methods have the same order that are dependent of type,
     * this method will obtain the index into that order.
     * @param type The Types instance used to get the index
     * @return The index into this order, starting at 0 = object, 1 = boolean, ... 9 = void
     */
    private static int getIndexFromType(Types type) {
        if (type.equals(Types.BOOLEAN)) return 1;
        if (type.equals(Types.BYTE)) return 2;
        if (type.equals(Types.CHAR)) return 3;
        if (type.equals(Types.SHORT)) return 4;
        if (type.equals(Types.INT)) return 5;
        if (type.equals(Types.LONG)) return 6;
        if (type.equals(Types.FLOAT)) return 7;
        if (type.equals(Types.DOUBLE)) return 8;
        if (type.equals(Types.VOID)) return 9;

        // object type if it hasn't matched yet
        return 0;
    }

    private static JNIOffsets[] setInstanceFields = { SET_INSTANCE_OBJECT_FIELD, GET_STATIC_BOOLEAN_FIELD,
        GET_STATIC_BYTE_FIELD, GET_STATIC_CHAR_FIELD, GET_STATIC_SHORT_FIELD, GET_STATIC_INT_FIELD,
        GET_STATIC_LONG_FIELD, GET_STATIC_FLOAT_FIELD, GET_STATIC_DOUBLE_FIELD };
    /**
     * Returns the JNI offsets instance used to set an object's field
     * @param type The types instance to determine which SET_&lt;TYPE&gt;_FIELD
     * @return The JNI Offsets instance to use in the function pointer
     */
    public static JNIOffsets setInstanceFieldOffset(Types type) {
        return setInstanceFields[getIndexFromType(type)];
    }

    private static JNIOffsets[] getStaticFields = { GET_STATIC_OBJECT_FIELD, GET_STATIC_BOOLEAN_FIELD,
        GET_STATIC_BYTE_FIELD, GET_STATIC_CHAR_FIELD, GET_STATIC_SHORT_FIELD, GET_STATIC_INT_FIELD,
        GET_STATIC_LONG_FIELD, GET_STATIC_FLOAT_FIELD, GET_STATIC_DOUBLE_FIELD };
    /**
     * Gets the offset for the GetStatic&lt;Type&gt;Field calls
     * @param type The JNI type that is used to determine the offset
     * @return JNIOffsets instance used for the offset.
     */
    public static JNIOffsets getStaticFieldOffset(Types type) {
        return getStaticFields[getIndexFromType(type)];
    }

    private static JNIOffsets[] setStaticFields = { SET_STATIC_OBJECT_FIELD, SET_STATIC_BOOLEAN_FIELD,
        SET_STATIC_BYTE_FIELD, SET_STATIC_CHAR_FIELD, SET_STATIC_SHORT_FIELD, SET_STATIC_INT_FIELD,
        SET_STATIC_LONG_FIELD, SET_STATIC_FLOAT_FIELD, SET_STATIC_DOUBLE_FIELD };
    /**
     * Gets the JNI offsets instance used  for the SetStatic&lt;Type&gt;Field calls
     * @param type The JNI type that is used to determine the offset
     * @return JNIOffsets instance used for the offset.
     */
    public static JNIOffsets setStaticFieldOffset(Types type) {
        return setStaticFields[getIndexFromType(type)];
    }

    private static JNIOffsets[] callMethods = { CALL_OBJECT_METHOD, CALL_BOOLEAN_METHOD,
        CALL_BYTE_METHOD, CALL_CHAR_METHOD, CALL_SHORT_METHOD, CALL_INT_METHOD,
        CALL_LONG_METHOD, CALL_FLOAT_METHOD, CALL_DOUBLE_METHOD,
        CALL_VOID_METHOD };
    /**
     * Gets the offset for the Call&lt;Type&gt;Method calls
     * @param type The JNI type that is used to determine the offset
     * @return The integer offset into the virtual function table (the index * pointer size)
     */
    public static JNIOffsets getCallMethodOffset(Types type) {
        return callMethods[getIndexFromType(type)];
    }

    private static JNIOffsets[] callNonVirtualMethods = { CALL_NON_VIRTUAL_OBJECT_METHOD,
        CALL_NON_VIRTUAL_BOOLEAN_METHOD, CALL_NON_VIRTUAL_BYTE_METHOD, CALL_NON_VIRTUAL_CHAR_METHOD,
        CALL_NON_VIRTUAL_SHORT_METHOD, CALL_NON_VIRTUAL_INT_METHOD, CALL_NON_VIRTUAL_LONG_METHOD,
        CALL_NON_VIRTUAL_FLOAT_METHOD, CALL_NON_VIRTUAL_DOUBLE_METHOD, CALL_NON_VIRTUAL_VOID_METHOD };
    /**
     * Gets the offset for the CallNonVirtual&lt;Type&gt;Method calls
     * @param type The JNI type that is used to determine the offset
     * @return The integer offset into the virtual function table (the index * pointer size)
     */
    public static JNIOffsets getCallNonVirtualMethodOffset(Types type) {
        return callNonVirtualMethods[getIndexFromType(type)];
    }

    private static JNIOffsets[] callStaticMethods = { CALL_STATIC_OBJECT_METHOD,
        CALL_STATIC_BOOLEAN_METHOD, CALL_STATIC_BYTE_METHOD, CALL_STATIC_CHAR_METHOD,
        CALL_STATIC_SHORT_METHOD, CALL_STATIC_INT_METHOD, CALL_STATIC_LONG_METHOD,
        CALL_STATIC_FLOAT_METHOD, CALL_STATIC_DOUBLE_METHOD, CALL_STATIC_VOID_METHOD };
    /**
     * Gets the offset for the CallStatic&lt;Type&gt;Method calls
     * @param type The JNI type that is used to determine the offset
     * @return The JNIOffsets instance that corresponds to that type passed in
     */
    public static JNIOffsets getCallStaticMethodOffset(Types type) {
        return callStaticMethods[getIndexFromType(type)];
    }
}
