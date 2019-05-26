
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

        // Total printout
        // -128
        // false
        // 10101111000011110010101000001111
        // 0
        // true
        // 255
    }
}