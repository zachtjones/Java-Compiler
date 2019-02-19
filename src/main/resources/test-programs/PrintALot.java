
/** This class simply calls system.out.print/println a lot */
public class PrintALot {
	public static void main(String[] args) {

		// This is designed to use all the registers in a different way, one where they all have
		//  about the same priority, so a lot more variety of cases on the pseudo instructions
		// So some will be mapped to real ones in different ways (real vs base pointer)
		long temp = 5L - 1L; // should get mapped to regs
		System.out.print(temp);
		System.out.print("a");
		System.out.print(8L - 2L); // one mapped to reg
		System.out.print("b");
		System.out.print("c");
		System.out.print("d");
		System.out.print("e");
		System.out.print(1L + 5L); // neither mapped to reg
		long temp2 = 5L;
		System.out.print(temp2 - 2L); // a different usage of it
		System.out.println();
	}
}