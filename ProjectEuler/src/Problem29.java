import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;


public class Problem29 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger currentNum = new BigInteger("1");
		Set<BigInteger> setOfNums = new HashSet<BigInteger>();
		for (int a = 2; a <= 100; a++) {
			for (int b = 2; b <= 100; b++) {
				currentNum = new BigInteger("" + a).pow(b);
				setOfNums.add(currentNum);
			}
		}
		
		System.out.println(setOfNums.size());

	}

}
