package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MthToLast {

	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
            String[] stringNums = line.split(" ");
            int numNums = stringNums.length - 1, index = Integer.parseInt(stringNums[numNums]);
            
            if (index <= numNums) {
            	System.out.println(stringNums[numNums-index]);
            }
        }
        
        buffer.close();
	}

}
