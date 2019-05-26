
/** This one tests the *, %, and / operators */
public class MultiplicativeOperators {
    public static void main(String[] args) {
        System.out.println(5 * 6); // 30
        int i = 40;
        System.out.println(i * -2); // -80
        int j = 5;

        System.out.println(-i * -j); // 200

        System.out.println(i / j); // 8
        System.out.println(i % j); // 0

        System.out.println(j / i); // 0
        System.out.println(j % i); // 5

        System.out.println(-j % i); // -5
        System.out.println(-10 % -8); // -2
        System.out.println(-10 % 8 ); // -2
    }
}