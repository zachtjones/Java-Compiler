public class Main {
    static { System.loadLibrary("Main"); }
    
    static native int mainMethod(String[] args);

    public static void main(String[] args){
        System.out.println(mainMethod(args));
    }
}
