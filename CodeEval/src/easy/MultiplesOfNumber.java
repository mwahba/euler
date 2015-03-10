package easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MultiplesOfNumber {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            String[] lineContents = line.split(",");
            int limit = Integer.parseInt(lineContents[0]), multiplicand = Integer.parseInt(lineContents[1]), currentNum = 1;
            while (currentNum * multiplicand < limit) {
            	currentNum++;
            }
            System.out.println(currentNum * multiplicand);
        }
        
        buffer.close();

	}

}
