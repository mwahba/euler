import java.util.ArrayList;
import java.util.List;


public class Problem19 {
	static List<Integer> thirty = new ArrayList<Integer>();
	
	public static void main (String[] args) {
		int currentYear = 1901, currentMonth = 1, currentDay = 1, numSundaysFirst = 0, dayCounter = 1;
		thirty.add(4);
		thirty.add(6);
		thirty.add(9);
		thirty.add(11);
		
		while ((currentYear != 2001) && (currentMonth != 12) && (currentDay != 31)) {
			dayCounter++;
			currentDay++;
			
			if (thirty.contains(currentMonth)) {
				if (currentDay == 31) {
					currentDay = 1;
					currentMonth++;
				}
			} else if (currentMonth == 2) {
				if ((currentYear % 4 != 0) || (currentYear % 400 == 0)) {
					if (currentDay == 29) {
						currentDay = 1;
						currentMonth++;
					}
				} else {
					if (currentDay == 30) {
						currentDay = 1;
						currentMonth++;
					}
				}
			} else if (currentDay == 32) {
				currentDay = 1;
				currentMonth++;
				
				if (currentMonth == 12) {
					currentMonth = 1;
					currentYear++;
					System.out.print(currentYear + ", ");
				}
			}
			
			if (dayCounter % 7 == 0) {
				if (currentDay == 1) {
					numSundaysFirst++;
				}
				dayCounter = 1;
			}
		}
		
		System.out.println(numSundaysFirst);
	}
}
