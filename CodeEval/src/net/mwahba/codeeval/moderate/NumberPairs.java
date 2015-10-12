package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NumberPairs {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line, toPrint = "";
        String[] splitLine;
        int goalSum, numNums;
        int[] nums;
        
        while ((line = buffer.readLine()) != null) {
        	splitLine = line.split(";");
        	goalSum = Integer.parseInt(splitLine[1]);
        	
        	splitLine = splitLine[0].split(",");
        	
        	nums = new int[numNums = splitLine.length];
        	
        	int localNum, secondNum;
        	
        	for (int i = 0; i < numNums; i++) {
        		// since all numbers given are going to be positive integers
        		if ((localNum = Integer.parseInt(splitLine[i])) <= goalSum) {
        			nums[i] = localNum;
        		} else {
        			nums[i] = -1;
        		}
        	}
        	
        	boolean anyCurrent = false;
        	
        	for (int i = 0; i < numNums; i++) {
        		localNum = nums[i];
        		for (int j = i + 1; j < numNums; j++) {
        			if ((secondNum = nums[j]) > 0 && (localNum + secondNum) == goalSum) {
        				if (anyCurrent) {
        					toPrint += ";";
        				} else {
        					anyCurrent = true;
        				}
        				toPrint += localNum + "," + secondNum;
        			}
        		}
        	}
        	
        	if (!anyCurrent) {
        		toPrint += "NULL";
        	}
        	
        	toPrint += "\n";
        }
        
        System.out.print(toPrint);
        
        buffer.close();
	}
}
