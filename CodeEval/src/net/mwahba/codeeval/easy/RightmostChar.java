package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RightmostChar {

	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            char character = line.split(",")[1].charAt(0);
            String statement = line.split(",")[0];
            
            for (int index = statement.length() - 1; index >= 0; index--) {
            	if (statement.charAt(index) == character) {
            		System.out.println(index);
            		index = 0;
            	} else if (index == 0 && statement.charAt(index) != character) {
            		System.out.println("-1");
            	}
            }
        }
        
        buffer.close();
	}

}
