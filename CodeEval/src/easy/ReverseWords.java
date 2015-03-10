package easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReverseWords {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            String[] lineContents = line.split(" ");
            String reversedContents = "";
            for (int i = 0; i < lineContents.length; i++) {
            	reversedContents = lineContents[i] + " " + reversedContents;
            }
            System.out.println(reversedContents);   
        }
        
        buffer.close();
	}

}
