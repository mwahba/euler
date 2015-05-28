package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DetectingCycles {

	public static boolean isCycle(int startLoc, int endLoc, String[] container) {
		int cycleLength = endLoc - startLoc;
		
		for (int currLoc = startLoc; currLoc < startLoc + cycleLength; currLoc++) {
			if (!container[currLoc].equals(container[currLoc + cycleLength])) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
            String[] stringNums = line.split(" ");
            int numNums = stringNums.length;
            
            int cycStart = -1, cycEnd = -1;
            
            for (int outerChar = 0; outerChar < numNums - 1; outerChar++) {
            	for (int innerChar = outerChar + 1; innerChar < numNums; innerChar++) {
            		if (stringNums[outerChar].equals(stringNums[innerChar])) {
            			if (isCycle(outerChar, innerChar, stringNums)) {
            				cycStart = outerChar;
                			cycEnd = innerChar - 1;
                			outerChar = numNums;
                			innerChar = numNums;
            			}
            		}
            	}
            }
            
            String toPrint = "";
            while (cycStart <= cycEnd) {
            	toPrint += stringNums[cycStart];
            	if (cycStart != cycEnd) {
            		toPrint += " ";
            	}
            	
            	cycStart++;
            }
            
            System.out.println(toPrint);
        }
        
        buffer.close();
	}

}
