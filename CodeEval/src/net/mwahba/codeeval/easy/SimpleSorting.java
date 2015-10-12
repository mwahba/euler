package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SimpleSorting {
	
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line, toPrint;
        String[] splitLine;
        List<Float> currentLineList = new ArrayList<Float>();
        
        while ((line = buffer.readLine()) != null) {
        	splitLine = line.split(" ");
        	currentLineList.clear();
        	
        	for (String numString : splitLine) {
        		currentLineList.add(Float.parseFloat(numString));
        	}
        	
        	Collections.sort(currentLineList);
        	
        	Iterator<Float> listIterator = currentLineList.iterator();
        	
        	toPrint = "";
        	
        	while (listIterator.hasNext()) {
        		String currentNum = listIterator.next().toString() + "000";
        		
        		toPrint += currentNum.substring(0, currentNum.indexOf('.') + 4);
        		if (listIterator.hasNext()) {
        			toPrint += " ";
        		}
        	}
        	
        	System.out.println(toPrint);
        }
        
        buffer.close();
	}
}
