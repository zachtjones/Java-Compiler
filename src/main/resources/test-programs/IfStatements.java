
public class IfStatements {

    public static void value(long a) {
        String value;
        if (a == 6L) {
            value = "a is 6";
        } else if (a == 7L) {
            value = "a is 7";
        } else if (a > 0L) {
            value = "a is positive";
        } else {
            value = "a is negative";
        }
        System.out.println(value);
    }

    public static void main(String[] args) {
        value(6L);
        value(7L);
        value(8L);
        value(10L - 283L);
    }
}