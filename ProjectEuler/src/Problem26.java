import java.math.BigDecimal;
import java.text.DecimalFormat;


public class Problem26 {

	public static String roundingFormat(int num) {
		String result = "0.";
		for (int i = 0; i < num; i++) {
			result = result.concat("#");
		}
		
		return result;
	}
	
	private static String getPattern(String resultString, int num) {
		if (resultString.length() > num+2) {
			int starting = 2;
			while (resultString.charAt(starting) == '0') {
				starting++;
			}
			
			String decimalString = resultString.substring(starting);
			
			String patternFound = decimalString.substring(0, 2);
			
			int lastFoundPosition = 0, searchPosition = 1;
			
			while (decimalString.indexOf(patternFound, searchPosition) > lastFoundPosition) {
				searchPosition = decimalString.indexOf(patternFound, searchPosition);
				lastFoundPosition = searchPosition;
				patternFound = decimalString.substring(0, searchPosition - 1);
			}
			
			return patternFound;
		}
		
		return "";
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String longest = "0.5";
		int num = 2, longestNum = 2;
		
		while (num < 1000) {
			try {
				BigDecimal currentResult = new BigDecimal(1).divide(new BigDecimal("" + num), num*2-2, BigDecimal.ROUND_DOWN);
				String resultString = new DecimalFormat(roundingFormat(num*2-2)).format(currentResult);
				String pattern = getPattern(resultString, num);
				if (pattern.length() > longest.length()) {
					longestNum = num;
					longest = pattern;
				}
			} catch (ArithmeticException ae){
				ae.printStackTrace();
			}
			num++;
		}
		
		System.out.println(longestNum);
	}

}
