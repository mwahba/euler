package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RemoveCharacters {
	
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
        	String string = line.split(", ")[0];
        	char[] charsToRemove = line.split(", ")[1].toCharArray();
        	
        	for (Character c : charsToRemove) {
        		string = string.replaceAll(c + "", "");
        	}
        	
        	System.out.println(string);
        	
        }
        
        buffer.close();
	}
}
