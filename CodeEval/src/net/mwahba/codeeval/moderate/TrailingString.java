package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TrailingString {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
        	String[] words = line.split(",");
        	String sentence = words[0], word = words[1];
        	if (sentence.contains(word) && sentence.substring(sentence.indexOf(word), sentence.length()).equals(word)) {
        		System.out.println(1);
        	} else {
        		System.out.println(0);
        	}
        }
        
        buffer.close();
	}
}
