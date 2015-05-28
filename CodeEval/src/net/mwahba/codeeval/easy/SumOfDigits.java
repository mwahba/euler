package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SumOfDigits {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            char[] digitChars = line.toCharArray();
            int sum = 0;
            for (int i = 0; i < digitChars.length; i++) {
            	sum += digitChars[i] - '0';
            }
            System.out.println(sum);
        }
        
        buffer.close();
	}

}
