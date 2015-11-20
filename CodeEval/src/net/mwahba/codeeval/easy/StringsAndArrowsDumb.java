package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StringsAndArrowsDumb {

	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line, toPrint = "";
        int numArrows, frontArrowPosition, backArrowPosition;
        
        while ((line = buffer.readLine()) != null) {
        	numArrows = 0;
        	frontArrowPosition = -2;
        	backArrowPosition = -2;
        	
        	for (int i = 0; i < line.length(); i++) {
        		if (frontArrowPosition > -1 || frontArrowPosition == -2) {
        			frontArrowPosition = line.indexOf("<--<<", i);
        			
        			if (frontArrowPosition > -1) {
            			numArrows++;
            			
            			i = frontArrowPosition;
            		}
        		}
        	}
        	
        	for (int i = 0; i < line.length(); i++) {
        		
        		if (backArrowPosition > -1 || backArrowPosition == -2) {
        			backArrowPosition = line.indexOf(">>-->", i);
        			
        			if (backArrowPosition > -1) {
            			numArrows++;
            			i = backArrowPosition;
            		}
        		}
        	}
        	
        	toPrint += numArrows + "\n";
        }
        
        System.out.print(toPrint);
        
        buffer.close();
	}

}
