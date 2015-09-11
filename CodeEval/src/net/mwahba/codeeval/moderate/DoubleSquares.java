package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DoubleSquares {
	public static void main(String[] args) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(new File(args[0])));
        String line;
        
        int numOfInts = 0;
        if ((line = buffer.readLine()) != null) {
        	numOfInts = Integer.parseInt(line);
        }
        
        while ((line = buffer.readLine()) != null && numOfInts > 0) {
        	Integer num = Integer.parseInt(line), upper = (int) Math.round(Math.sqrt(num)), 
        			diff = num - (int) Math.pow(upper, 2);
        	double lowerSqrt = Math.sqrt(diff);
        	int lower = (int) Math.round(lowerSqrt), numDoubleSquares = 0;
        	
        	while (upper >= 0) {
        		diff = num - (int) Math.pow(upper, 2);
        		lowerSqrt = Math.sqrt(diff);
        		lower = (int) Math.round(lowerSqrt);
        		
        		if (upper >= lower && (lowerSqrt - lower == 0.0) && 
        				(Math.round(Math.pow(upper,  2) + Math.pow(lower, 2)) == num)) {
        			numDoubleSquares++;
        		}
        		
        		upper--;
        	}
        	
        	System.out.println(numDoubleSquares);
        	
        	numOfInts--;
        }
        buffer.close();
	}
}
