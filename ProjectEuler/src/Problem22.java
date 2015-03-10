import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;


public class Problem22 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String[] names = {};
		BigInteger result = new BigInteger("0");
		
		try {
			FileReader file = new FileReader("p022_names.txt");
			
			BufferedReader reader = new BufferedReader(file);
			
			String line = reader.readLine();
			
			names = line.split("\",\"");
			names[0] = names[0].replace("\"", "");
			names[names.length-1] = names[names.length-1].replace("\"", "");
			
			Arrays.sort(names); 
			
			for (Integer i = 0; i < names.length; i++) {
				Integer sumOfCurrentName = 0;
				String name = names[i];
				for (Character c : name.toCharArray()) {
					sumOfCurrentName += ((c - 'A') + 1);
				}
				
				if (name.equals("COLIN")) {
					System.out.println("Found COLIN: " + sumOfCurrentName + " x " + (i + 1) + " = " + new BigInteger((new Integer(i + 1)).toString()).multiply(new BigInteger(sumOfCurrentName.toString())));
				}
				
				result = result.add(new BigInteger((new Integer(i + 1)).toString()).multiply(new BigInteger(sumOfCurrentName.toString())));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}

}
