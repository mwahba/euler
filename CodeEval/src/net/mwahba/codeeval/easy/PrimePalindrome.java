package net.mwahba.codeeval.easy;

public class PrimePalindrome {

	public static boolean isPrime(int n) {
		if (n <= 3) {
	        return n > 1;
	    } else if (n % 2 == 0 || n % 3 == 0) {
	        return false;
	    } else {
	        for (int i = 5; i * i <= n; i += 6) {
	            if (n % i == 0 || n % (i + 2) == 0) {
	                return false;
	            }
	        }
	        return true;
	    }
	}
	
	public static boolean isNumPalindrome(int num) {
		int reverseNum = 0, origNum = num;
		
		while (num > 10) {
			reverseNum += (num % 10);
			reverseNum *= 10;
			num /= 10;
		}
		reverseNum += (num % 10);
		
		return (reverseNum == origNum);
	}
	
	public static void main(String[] args) {
		int maxPrimePalindrome = 1;
		for (int currentNum = 1; currentNum < 1000; currentNum++) {
			if (isNumPalindrome(currentNum)) {
				if (isPrime(currentNum)) {
					maxPrimePalindrome = currentNum;
				}
			}
		}
		
		System.out.println(maxPrimePalindrome);
	}

}
