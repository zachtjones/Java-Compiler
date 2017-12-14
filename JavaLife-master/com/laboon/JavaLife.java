package com.laboon;

public class JavaLife {
	
	/**
	 *
	 * @param size Size of world
	 * @param seed Random number seed
	 * @param percent Percent of cells alive at beginning
	 * @param maxIterations Maximum number of iterations
	 */
	
	public JavaLife(int size, int seed, int percent, int maxIterations) {
		World w = new World(size, seed, percent);
		System.out.println("Initial Configuration:");
		System.out.println(w.toString());
		for (int j=0; j < maxIterations; j++) {
			w = w.iterate();
			System.out.println("Iteration " + (j + 1) + ":");
			System.out.println(w.toString());
		}
		
	}
	
	/**
	 * String to display if wrong number of arguments or other unspecified error
	 * when reading command line arguments.
	 * @return String to display
	 */
	
	private static String getErrMessage() {
		return "Requires four args: <size> <seed> <percent> <max iterations>";
	}
	
	/**
	 * String to display if command line args are not formatted properly.
	 * @return String to display
	 */
	
	private static String getNumFormatMessage() {
		return "Please enter four positive integers for the arguments.";
	}
	
	/**
	 * MAIN
	 * @param args Command line arguments
	 *             [0] -> Size of world
	 *             [1] -> Random number seed
	 *             [2] -> Percent of cells alive
	 *             [3] -> Maximum number of iterations
	 */
	
	public static void main(String[] args) {
		if (args.length != 4) {
			System.err.println(getErrMessage());
			System.exit(1);
		}
		
		int size = 0, seed = 0, percent = 0, maxIterations = 0;
		
		try {
			size          = Integer.parseInt(args[0]);
			seed          = Integer.parseInt(args[1]);
			percent       = Integer.parseInt(args[2]);
			maxIterations = Integer.parseInt(args[3]);
			if (size < 0 || seed < 0 || percent < 0 || maxIterations < 0) {
				throw (new NumberFormatException());
			}
		} catch (NumberFormatException nfex) {
			System.err.println(getNumFormatMessage());
			System.exit(1);
		}
		JavaLife jl = new JavaLife(size, seed, percent, maxIterations);
	}
	
}
