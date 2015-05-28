package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SelfDescribingNums {
	
	public static int countOf(int num, String string) {
		int result = 0;
		
		for (int i = 0; i < string.length(); i++) {
			if (num == Integer.parseInt("" + string.charAt(i))) {
				result++;
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
           line = line.trim();
           boolean selfDescribingNumber = true;
           for (int i = 0; i < line.length(); i++) {
        	   if (countOf(i, line) != Integer.parseInt("" + line.charAt(i))) {
        		   System.out.println(0);
        		   i = line.length();
        		   selfDescribingNumber = false;
        	   }
           }
           
           if (selfDescribingNumber)
        	   System.out.println(1);
        }
        
        buffer.close();
	}
}
