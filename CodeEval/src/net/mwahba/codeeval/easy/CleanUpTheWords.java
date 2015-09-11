package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CleanUpTheWords {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
        	String[] words = line.toLowerCase().split("[^a-zA-Z]");
        	
        	String toPrint = "";
        	
        	for (String word : words) {
        		if (word.length() > 0) {
        			toPrint += word + " ";
        		}
        	}
        	
        	System.out.println(toPrint);
        }
        
        buffer.close();
	}
}
