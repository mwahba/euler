package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NumberOfOnes {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
        	String numAsBin = Integer.toBinaryString(Integer.parseInt(line));
        	System.out.println(numAsBin.length() - numAsBin.replaceAll("1", "").length());
        }
        
        buffer.close();
	}
}
