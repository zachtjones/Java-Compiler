/** This program tests the pre and post incrementing functionality */
public class Incrementing {
    public static void main(String[] args) {
        int i=1;
        int j=1;
        int k=1;
        int l=1;

        System.out.println(i++ + i++);
        System.out.println(++j + ++j);
        System.out.println(k++ + ++k);
        System.out.println(++l + l++);
    }
}