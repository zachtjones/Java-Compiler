package x64;

public enum JNIOffsets {
    FIND_CLASS(6);

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
}
