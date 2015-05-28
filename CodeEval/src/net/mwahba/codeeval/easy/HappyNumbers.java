package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HappyNumbers {
	
	public static int sumOfSquaresOfDigits(int num) {
		int result = 0;
		
		while (num > 0) {
			result += (num % 10) * (num % 10);
			num /= 10;
		}
		
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            Integer num = Integer.parseInt(line);
            
            List<Integer> alreadyIterated = new ArrayList<Integer>();
            alreadyIterated.add(num);
            
            while (num != 1 && num != 0) {
            	num = sumOfSquaresOfDigits(num);
            	//System.out.print(num + "->");
            	if (alreadyIterated.contains(num)) {
            		num = 0;
            	} else {
            		alreadyIterated.add(num);
            	}
            }
            
            System.out.println(num);
        }
        
        buffer.close();
	}
}
