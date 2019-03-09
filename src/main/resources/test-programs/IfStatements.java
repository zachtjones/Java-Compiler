
public class IfStatements {

    public static void value(long a) {
        if (a == 6L) {
            System.out.println("a is 6");
        } else if (a == 7L) {
            System.out.println("a is 7");
        } else if (a > 0L) {
            System.out.println("a is positive");
        } else {
            System.out.println("a is negative");
        }
    }

    public static void main(String[] args) {
        value(6L);
        value(7L);
        value(8L);
        value(10L);
        value(-283L);
    }
}