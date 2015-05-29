package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ArmstrongNumber {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line, response = "";
        
        while ((line = buffer.readLine()) != null) {
        	Integer n = line.length(), currentSum = 0, num = Integer.parseInt(line);
        	
        	for (int numLoc = 0; numLoc < n; numLoc++) {
        		currentSum += (int) Math.pow(line.charAt(numLoc) - 48, n);
        	}
        	
        	if (currentSum.equals(num)) {
        		response += "True\n";
        	} else {
        		response += "False\n";
        	}
        }
        
        System.out.print(response);
        buffer.close();
	}
}
