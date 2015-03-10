import java.math.BigInteger;


public class Problem20 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger answer = new BigInteger("1");
		for (int i = 1; i <= 100; i++) {
			answer = answer.multiply(new BigInteger(Integer.toString(i)));
		}
		
		int result = 0;
		
		for (Character c : answer.toString().toCharArray()) {
			result += (c - '0');
		}
		
		System.out.println(result);
	}

}
