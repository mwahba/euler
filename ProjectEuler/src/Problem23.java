import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Problem23 {
	
	private static Set<Integer> findFactors(int num) {
		Set<Integer> factors = new HashSet<Integer>();
		
		for (int i = 1; i < num; i++) {
			if (num % i == 0) {
				if (!factors.contains(i)) {
					factors.add(i);
					if (i > 1) {
						factors.add(num / i);
					}
				}
			}
		}
		
		return factors;
	}
	
	private static Integer sum(Set<Integer> set) {
		Integer result = 0;
		Iterator<Integer> setIterator = set.iterator();
		
		while (setIterator.hasNext()) {
			result += setIterator.next();
		}
		
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> abundantNumbers = new ArrayList<Integer>();
		Set<Integer> sumsOfAbundantNumbers = new HashSet<Integer>();
		
		for (int i = 12; i < 28123; i++) {
			Set<Integer> factors = findFactors(i);
			Integer factorsSum = sum(factors);
			
			if (factorsSum > i) {
				abundantNumbers.add(i);
				
				for (int j = 0; j < abundantNumbers.size(); j++) {	
					Integer newSum = i + abundantNumbers.get(j);
					sumsOfAbundantNumbers.add(newSum);
				}
			}
			
			if (i % 1000 == 0) {
				System.out.println(i);
			}
		}
		
		Integer sumOfAllOthers = 0;
		
		for(int i = 1; i <= 28123; i++) {
			if (!sumsOfAbundantNumbers.contains(i)) {
				sumOfAllOthers += i;
			}
		}
		
		System.out.println(sumOfAllOthers);
	}

}
