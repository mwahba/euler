package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StringsAndArrows {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line, toPrint = "";
        int numArrows;
        
        while ((line = buffer.readLine()) != null) {
        	numArrows = 0;
        	
        	int forwardArrow = 0, backArrow = 0;
        	
        	for (char c: line.toCharArray()) {
        		
        		switch (c) {
        		case '<':
        			switch(forwardArrow) {
        			case 0:
        				backArrow = 0;
        			case 3:
        				forwardArrow++;
        			case 2:
        				break;
        			case 4:
        				numArrows++;
    				default:
    					forwardArrow = 1;
    					break;
        			}
        			break;
        		case '-':
        			switch (forwardArrow) {
        			case 1:
        			case 2:
        				forwardArrow++;
        			default:
        				forwardArrow = 0;
        				backArrow = 0;
        				break;
        			}
        			switch (backArrow) {
        			case 2:
        			case 3:
        				backArrow++;
        			default:
        				forwardArrow = 0;
        				backArrow = 0;
        				break;
        			}
        			break;
        		case '>':
        			switch(backArrow) {
        			case 0:
        				forwardArrow = 0;
        			case 1:
        				backArrow++;
        			case 2:
        				break;
        			case 4:
        				numArrows++;
    				default:
    					backArrow = 1;
    					break;
        			}
        			break;
    			default:
    				forwardArrow = 0;
    				backArrow = 0;
    				break;
        		}
        	}
        	
        	toPrint += numArrows + "\n";
        }
        
        System.out.print(toPrint);
        
        buffer.close();
	}
}
