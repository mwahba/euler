import java.math.BigInteger;


public class Problem30 {

	private static final int POWER = 5;
	
	private static int sumOfPowers(int num) {
		if (num >= 10) {
			return (int) (Math.pow(num % 10, POWER) + sumOfPowers(num/10));
		} else {
			return (int) Math.pow(num, POWER);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger sumResultNums = new BigInteger("0");
		
		for (int num = 2; num < 200000; num++) {
			int numSum = sumOfPowers(num);
			if (numSum == num) {
				sumResultNums = sumResultNums.add(new BigInteger(numSum + ""));
				System.out.println(num + ": " + numSum);
			}
		}
		
		System.out.println(sumResultNums.toString());
	}

}
