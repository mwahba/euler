import java.util.HashSet;
import java.util.Set;


public class Problem32 {

	private static int checkPandigital(int multiplicand, int multiplier) {
		int product = multiplicand * multiplier;
		
		String combined = "" + multiplicand + multiplier + product;
		
		if (combined.length() != 9) {
			if (combined.length() > 9) {
				return 1;
			} else {
				return -1;
			}
		}
		
		/*char[] combinedCharArray = combined.toCharArray();
		
		Arrays.sort(combinedCharArray);
		
		for (int i = 1; i <= 9; i++) {
			if (combinedCharArray[i-1] != '0' + i) {
				return -1;
			}
		}*/
		
		if (!(combined.contains("1") && combined.contains("2") && combined.contains("3") && combined.contains("4") 
				&& combined.contains("5") && combined.contains("6") && combined.contains("7") 
				&& combined.contains("8") && combined.contains("9"))) {
			return -1;
		}
		
		return 0;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Set<String> pandigitalSet = new HashSet<String>();
		
		int product = 0;
		
		for (int multiplicand = 1; multiplicand <= 23456789; multiplicand++) {
			for (int multiplier = 1; multiplier <= 23456789; multiplier++) {
				product = checkPandigital(multiplicand, multiplier);
				if (product == 0) {
					if (!pandigitalSet.contains(multiplier + "x" + multiplicand)) {
						pandigitalSet.add(multiplicand + "x" + multiplier);
					}
				} else if (product > 0) {
					multiplier = 23456789;
				}
			}
		}
		
		for (String result : pandigitalSet) {
			String[] multipliersString = result.split("x");
			int multiplier = Integer.parseInt(multipliersString[1]), multiplicand = Integer.parseInt(multipliersString[0]);
			System.out.println(multiplicand + " x " + multiplier + " = " + (multiplier * multiplicand));
		}
		System.out.println(pandigitalSet.size());
	}

}
