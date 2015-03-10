import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Problem21 {

	private static List<Integer> findFactors(int num) {
		List<Integer> factors = new ArrayList<Integer>();
		
		for (int i = 1; i < num; i++) {
			if (num % i == 0) {
				if (factors.indexOf(i) < 0) {
					factors.add(i);
					if (i > 1) {
						factors.add(num / i);
					}
				}
			}
		}
		
		return factors;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<Integer, Integer> sumDivisors = new HashMap<Integer, Integer>();
		Integer sum = 0;
		
		for (int i = 1; i <= 10000; i++) {
			List<Integer> currentFactors = findFactors(i);
			Integer currentSum = 0;
			
			for (int j = 0; j < currentFactors.size(); j++) {
				currentSum += currentFactors.get(j);
			}
			
			sumDivisors.put(i, currentSum);
			
			if (sumDivisors.containsKey(currentSum)) {
				if ((sumDivisors.get(currentSum) == i) && (currentSum != i)) {
					sum += i;
					sum += currentSum;
					System.out.print(i + ", " + currentSum + ", ");
				}
			}
		}
		
		System.out.println(sum);
	}

}
