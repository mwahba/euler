package hard;

import java.math.BigInteger;
public class NumMovements {
	
	private final static int VALUE = 5; // Should be the grid's value + 1
	
	private static BigInteger[][] grid = new BigInteger[VALUE][VALUE];
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// setup initial grid
		for (int i = 0; i < VALUE; i++) {
			grid[0][i] = new BigInteger("1");
			grid[i][0] = new BigInteger("1");
		}
		
		for (int i = 1; i < VALUE; i++) {
			for (int j = 1; j < VALUE; j++) {
				grid[i][j] = grid[i-1][j].add(grid[i][j-1]);
			}
		}
		
		System.out.println(grid[VALUE-1][VALUE-1]);
	}

}
