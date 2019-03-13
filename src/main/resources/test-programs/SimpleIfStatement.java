
public class SimpleIfStatement {

    public static void value(long a) {
        if (a == 6L) {
            System.out.println("a is 6");
        }
        System.out.println("always run");
    }

    public static void main(String[] args) {
        value(6L);
        value(7L);
    }
}