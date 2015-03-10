import java.math.BigInteger;


public class Problem16 {
	
	public static void main (String[] args) { 
		BigInteger calcResult = new BigInteger("2");
		
		calcResult = calcResult.pow(1000);
		
		int result = 0;
		
		for (int i = 0; i < calcResult.toString().length(); i++) {
			result += Integer.parseInt(calcResult.toString().charAt(i) + "");
		}
		
		System.out.println(result);
	}

}
