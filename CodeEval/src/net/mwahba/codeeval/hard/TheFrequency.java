package net.mwahba.codeeval.hard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TheFrequency {

	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        int response;
        
        while ((line = buffer.readLine()) != null) {
        	String[] numsString = line.split(" ");
        	List<Integer> nums = new ArrayList<Integer>();
        	int max = Integer.parseInt(numsString[0]), min = Integer.parseInt(numsString[0]), 
        			lastNum = Integer.parseInt(numsString[0]), largestRange = 0;
        	
        	for (String stringNum : numsString) {
        		int currentNum = Integer.parseInt(stringNum);
        		if (max < currentNum) {
        			max = currentNum;
        		}
        		
        		if (min > currentNum) {
        			min = currentNum;
        		}
        		
        		if (Math.abs(currentNum - lastNum) > largestRange) {
        			largestRange = Math.abs(currentNum - lastNum);
        		}
        		
        		nums.add(currentNum);
        		
        		lastNum = currentNum;
        	}
        	
        	
        	
        	response = 0;
        	
        	System.out.println(response);
        }
        buffer.close();
	}

}
