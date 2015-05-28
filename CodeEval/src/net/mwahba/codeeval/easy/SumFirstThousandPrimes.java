package net.mwahba.codeeval.easy;

public class SumFirstThousandPrimes {

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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int sum = 0, currentPrimeCount = 0;
		
		for (int currentNum = 1; currentPrimeCount < 1000; currentNum++) {
			if (isPrime(currentNum)) {
				sum += currentNum;
				currentPrimeCount++;
			}
		}
		
		System.out.println(sum);

	}
}
