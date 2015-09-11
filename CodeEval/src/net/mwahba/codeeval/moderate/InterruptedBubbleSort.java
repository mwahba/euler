package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InterruptedBubbleSort {
	public static void main(String[] args) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(new File(args[0])));
        String line;
        
        while ((line = buffer.readLine()) != null) {
        	String[] numsString = line.split("\\|");
        	long iterations = Long.parseLong(numsString[1].trim());
        	
        	numsString = line.split("\\|")[0].trim().split(" ");
        	int[] nums = new int[numsString.length];
        	
        	for (int index = 0; index < numsString.length; index++) {
        		nums[index] = Integer.parseInt(numsString[index]);
        	}
        	
        	String sortedString = "";
        	
        	boolean sorting = true;
        	
        	while (iterations > 0 && sorting) {
        		sorting = false;
        		for (int i = 0; i < numsString.length - 1; i++) {
        			if (nums[i] > nums[i+1]) {
        				int largerNum = nums[i];
        				nums[i] = nums[i+1];
        				nums[i+1] = largerNum;
        				sorting = true;
        			}
        			
        			if (iterations == 1) {
        				sortedString += nums[i] + " ";
        			}
        		}
        		
        		iterations--;
        	}
        	
        	sortedString += nums[numsString.length -1];
        	
        	System.out.println(sortedString);
        }
        buffer.close();
	}
}
