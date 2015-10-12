package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SwapCase {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        int character;
        while ((character = buffer.read()) != -1) {
        	if (character > 64 && character < 91) {
        		character += 32;
        	} else if (character > 96 && character < 123) {
        		character -= 32;
        	}
        	
        	System.out.append((char) character);
        }
        
        buffer.close();
	}
}
