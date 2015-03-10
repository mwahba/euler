
public class Problem27 {

	public static boolean isPrime(long n) {
	    if (n <= 3) {
	        return n > 1;
	    } else if (n % 2 == 0 || n % 3 == 0) {
	        return false;
	    } else {
	        double sqrtN = Math.floor(Math.sqrt(n));
	        for (int i = 5; i <= sqrtN; i += 6) {
	            if (n % i == 0 || n % (i + 2) == 0) {
	                return false;
	            }
	        }
	        return true;
	    }
	}
	
	public static int getNumPrimes (int a, int b) {
		int numPrimes = 0;
		for (int i = 0; i < 1000; i++) {
			long numInQuestion = (i * i) + (i * a) + b;
			
			if (isPrime(numInQuestion)) {
				numPrimes++;
			} else {
				return numPrimes;
			}
		}
		return numPrimes;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int maxA = -999, maxB = -999, maxNumPrimes = 0;
		
		for (int a = -999; a < 1000; a++) {
			for (int b = -999; b < 1000; b++) {
				int currentNumPrimes = getNumPrimes(a, b);
				if (currentNumPrimes > maxNumPrimes) {
					maxA = a;
					maxB = b;
					maxNumPrimes = currentNumPrimes;
				}
			}
			if (a % 100 == 0) {
				System.out.println(a);
			}
		}
		
		System.out.println("Max num primes: " + maxNumPrimes + ", a: " + maxA + ", b: " + maxB);
	}

}
