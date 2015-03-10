import java.math.BigInteger;
import java.util.HashMap;

public class Problem14 {
	
	private static final HashMap<BigInteger, Integer> previousLengths = new HashMap<BigInteger, Integer>();

	public static int lengthOfChain(BigInteger num) {
		BigInteger currentNum = num;

		if (previousLengths.containsKey(num)) {
			return previousLengths.get(num);
		} else {
			if (num.equals(new BigInteger("1"))) {
				previousLengths.put(new BigInteger("1"), 1);
				return 1;
			} else if (currentNum.mod(new BigInteger("2")).equals(BigInteger.ZERO)) {
				currentNum = currentNum.divide(new BigInteger("2"));
			} else {
				currentNum = (currentNum.multiply(new BigInteger("3"))).add(new BigInteger("1"));
			}
			
			if (!previousLengths.containsKey(num)) {
				previousLengths.put(num, lengthOfChain(currentNum) + 1);
			}
			
			return lengthOfChain(currentNum) + 1;
		}
	}
	
	public static int lengthOfChain (int num) {
		return lengthOfChain(BigInteger.valueOf(num));
	}

	public static void main (String[] args) {
		int longest = -1, element = 0;

		for (int i = 1; i < 1000000; i += 2) {
			int length = lengthOfChain(i);
			if (length > longest) {
				longest = length;
				element = i;
			}
		}

		System.out.println(element + ": " + longest);
	}
}