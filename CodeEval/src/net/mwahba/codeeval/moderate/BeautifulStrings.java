package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BeautifulStrings {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
        	line = line.toLowerCase().replaceAll("[^a-z]", "");
        	Integer stringScore = 0;
        	
        	for (char c : line.toCharArray()) {
        		stringScore += (c - 96);
        	}
        	
        	System.out.println(stringScore);
        }
        
        buffer.close();
	}
}
