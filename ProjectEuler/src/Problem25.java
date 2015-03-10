import java.math.BigInteger;


public class Problem25 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger n1 = new BigInteger("1"), n2 = new BigInteger("1"), n3 = new BigInteger("2");
		int count = 3;
		
		while (("" + n3).length() < 1000) {
			n1 = n2;
			n2 = n3;
			n3 = n1.add(n2);
			
			count++;
		}
		
		System.out.println(count);
	}

}
