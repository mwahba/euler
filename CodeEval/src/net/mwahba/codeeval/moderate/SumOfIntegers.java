package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SumOfIntegers {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
        	String stringNums[] = line.split(",");
        	Integer largestSum = Integer.MIN_VALUE, nums[] = new Integer[stringNums.length];
        	
        	for (int numLoc = 0; numLoc < stringNums.length; numLoc++) {
        		nums[numLoc] = Integer.parseInt(stringNums[numLoc]);
        	}
        	
        	for (int i = 0; i < nums.length; i++) {
        		Integer currentSum = 0;
        		for (int f = i; f < stringNums.length; f++) {
        			currentSum += nums[f];
        			
        			if (currentSum > largestSum) {
        				largestSum = currentSum;
        			}
        		}
        	}
        	
        	System.out.println(largestSum);
        }
        
        buffer.close();
	}
}
