package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StackImplementation {

	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
            String[] stringNums = line.split(" ");
            int numNums = stringNums.length;
                       
            String toPrint = "";
            boolean alternate = true;
            
            for (int curr = numNums - 1; curr >= 0; curr--) {
            	if (alternate) {
            		toPrint += stringNums[curr];
            		if (curr > 1) {
            			toPrint += " ";
            		}
            	}
            	
            	alternate = !alternate;
            }
            System.out.println(toPrint);
        }
        
        buffer.close();

	}

}
