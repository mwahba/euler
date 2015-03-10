package easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BitPositions {

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
            int firstPosition = Integer.parseInt(lineContents[1]) - 1, secondPosition = Integer.parseInt(lineContents[2]) - 1;
            String bitNum = Integer.toBinaryString(Integer.parseInt(lineContents[0]));
            if (bitNum.charAt(firstPosition) == bitNum.charAt(secondPosition)) {
            	System.out.println("true");
            } else {
            	System.out.println("false");
            }
        }
        
        buffer.close();
	}

}
