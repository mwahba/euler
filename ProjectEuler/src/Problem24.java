import java.util.SortedSet;
import java.util.TreeSet;


public class Problem24 {
	private static SortedSet<String> permutations = new TreeSet<String>();
	
	private static void makePermutations(String current, String numbersLeft) {
		if (numbersLeft.equals("")) {
			permutations.add(current);
		} else {
			for (int i = 0; i < numbersLeft.length(); i++) {
				Character number = numbersLeft.charAt(i);
				String newString = current.concat("" + number),
				numbersLeftNew = numbersLeft.replaceAll("" + number, "");
				makePermutations(newString, numbersLeftNew);
			}
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String numbers = "0123456789";
		
		int currentStartingNum = 0;
		
		makePermutations("" + currentStartingNum, numbers.substring(1));
		
		int permutationsPerNum = permutations.size(), currentCount = permutationsPerNum;
		
		while (currentCount < 1000000) {
			currentCount += permutationsPerNum;
			currentStartingNum++;
		}
		
		numbers = numbers.replaceAll("" + currentStartingNum, "");
		
		makePermutations("" + currentStartingNum, numbers);
		
		int millionthIndex = permutations.size() - (currentCount - 999999);  
		
		System.out.println(permutations.toArray()[millionthIndex]);
	}

}
