import java.math.BigInteger;
public class Problem15 {
	
	private static BigInteger[][] grid = new BigInteger[21][21];
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// setup initial grid
		for (int i = 0; i < 21; i++) {
			grid[0][i] = new BigInteger("1");
			grid[i][0] = new BigInteger("1");
		}
		
		for (int i = 1; i < 21; i++) {
			for (int j = 1; j < 21; j++) {
				grid[i][j] = grid[i-1][j].add(grid[i][j-1]);
			}
		}
		
		for (int i = 0; i < 21; i++) {
			for (int j = 0; j < 21; j++) {
				System.out.print(grid[i][j] + ", ");
			}
			System.out.println();
		}
	}

}
