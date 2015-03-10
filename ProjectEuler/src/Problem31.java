import java.util.HashSet;
import java.util.Set;


public class Problem31 {

	private static final int VALUE = 200;
	static Set<String> combinations = new HashSet<String>();	
	
	private static void createCombinations (int startingValue, int[] combination) {
		int currentVal = 200;
		for (int num : combination) {
			currentVal -= num;
		}
		
		currentVal -= startingValue;
		
		switch (startingValue) {
			case 1:
				combination[0] += 1;
				break;
			case 2:
				combination[1] += 2;
				break;
			case 5:
				combination[2] += 5;
				break;
			case 10:
				combination[3] += 10;
				break;
			case 20:
				combination[4] += 20;
				break;
			case 50:
				combination[5] += 50;
				break;
			case 100:
				combination[6] += 100;
				break;
		}
		
		if (currentVal > 0) {
			if (currentVal >= 100 && startingValue >= 100)
				createCombinations(100, combination.clone());
			if (currentVal >= 50 && startingValue >= 50)
				createCombinations(50, combination.clone());
			if (currentVal >= 20 && startingValue >= 20)
				createCombinations(20, combination.clone());
			if (currentVal >= 10 && startingValue >= 10)
				createCombinations(10, combination.clone());
			if (currentVal >= 5 && startingValue >= 5)
				createCombinations(5, combination.clone());
			if (currentVal >= 2 && startingValue >= 2)
				createCombinations(2, combination.clone());
			if (currentVal >= 1 && startingValue >= 1)
				createCombinations(1, combination.clone());
		} else {
			addCombination(combination);
		}
	}
	
	private static void addCombination(int[] combination) {
		int combinationSum = 0;
		String combinationString = "";
		for (int i = 0; i < combination.length; i++) {
			combinationSum += combination[i];
			combinationString += combination[i] + "\t";
		}
		
		if (combinationSum == 200)
			combinations.add(combinationString);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int currentCombination[] = new int[8];
		
		currentCombination[7] += VALUE;
		addCombination(currentCombination);
		
		currentCombination = new int[8];
		createCombinations(100, currentCombination);
		
		currentCombination = new int[8];
		createCombinations(50, currentCombination);
		
		currentCombination = new int[8];
		createCombinations(20, currentCombination);
		
		currentCombination = new int[8];
		createCombinations(10, currentCombination);
		
		currentCombination = new int[8];
		createCombinations(5, currentCombination);
		
		currentCombination = new int[8];
		createCombinations(2, currentCombination);
		
		currentCombination = new int[8];
		currentCombination[0] += VALUE;
		addCombination(currentCombination);
		
		System.out.println(combinations.size());
	}

}
