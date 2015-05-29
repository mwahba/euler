package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FirstNonRepeatedCharacter {

	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
            for (Character c : line.toCharArray()) {
            	if (line.indexOf(c) == line.lastIndexOf(c)) {
            		System.out.println(c);
            		break;
            	}
            }
        }
        
        buffer.close();
	}

}
