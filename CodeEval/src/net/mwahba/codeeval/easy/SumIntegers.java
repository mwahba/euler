package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SumIntegers {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        int sum = 0;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            sum += Integer.parseInt(line);
        }
        
        System.out.println(sum);
        
        buffer.close();
	}

}
