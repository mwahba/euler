
public class Problem17 {

	public static final String[] tens = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
	public static final String[] digits = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
			"eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	public static String numericToAlphabetical (int num) {
		
		if (num == 0) {
			return "";
		} else if (num < 20) {
			return digits[num-1];
		} else if (num < 100) {
			int tensDigit = num/10;
			int singlesDigit = num - (tensDigit * 10);
			return tens[tensDigit-2] + numericToAlphabetical(singlesDigit);
		} else if (num < 1000) {
			int hundredsDigit = num/100;
			int otherDigits = num - (hundredsDigit * 100);
			if (otherDigits > 0) {
				return digits[hundredsDigit-1] + "hundredand" + numericToAlphabetical(otherDigits);
			} else {
				return digits[hundredsDigit-1] + "hundred";
			}
		} else if (num == 1000) {
			return "onethousand";
		}
		
		return null;
	}
	
	public static void main (String[] args) {
		int total = 0;
		
		for (int i = 1; i <= 1000; i++) {
			System.out.println(i + ":\t" + numericToAlphabetical(i));
			total += numericToAlphabetical(i).length();
		}
		
		System.out.println(total);
	}
}
