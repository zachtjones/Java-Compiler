/** This program tests the pre and post decrementing functionality */
public class Decrementing {
    public static void main(String[] args) {
        int i=3;
        int j=3;
        int k=3;
        int l=3;

        System.out.println(i-- + i--); // 3 + 2
        System.out.println(--j + --j); // 2 + 1
        System.out.println(k-- + --k); // 3 + 1
        System.out.println(--l + l--); // 2 + 2
    }
}