public class Main {
    static { System.loadLibrary("Main"); }
    
    static native void mainMethod(String[] args);

    public static void main(String[] args){
        mainMethod(args);
    }
}
