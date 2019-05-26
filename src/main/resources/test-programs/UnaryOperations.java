
public class UnaryOperations {
    public static void main(String[] args) {
        System.out.println(-128);
        System.out.println(!true);
        System.out.println(Integer.toBinaryString(~0b0101_0000_1111_0000_1101_0101_1111_0000));

        int x = 20;
        int y = 0x0000;
        System.out.println(x + -x);
        System.out.println(!(x != 20));
        System.out.println((short)~y);

        System.out.println(-x);
        int x2 = ~x;
        System.out.println(!foo(x));
        System.out.println((long)x2);;
        System.out.println(+20); // also valid

        // Total printout
        // -128
        // false
        // 10101111000011110010101000001111
        // 0
        // true
        // 255
        // -20
        // true
        // -21
    }

    static boolean foo(int x) {
        return x == 15;
    }
}