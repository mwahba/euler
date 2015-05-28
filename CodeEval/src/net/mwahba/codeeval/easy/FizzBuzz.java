package net.mwahba.codeeval.easy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class FizzBuzz {

	public static void main (String[] args) throws IOException {
        File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            String[] lineContents = line.split(" ");
            int firstDivisor = Integer.parseInt(lineContents[0]), 
            	secondDivisor = Integer.parseInt(lineContents[1]), 
            	limit = Integer.parseInt(lineContents[2]);
            
            for (int currentNum = 1; currentNum <= limit; currentNum++) {
            	if (currentNum % firstDivisor == 0)
            		System.out.print("F");
            	if (currentNum % secondDivisor == 0)
            		System.out.print("B");
            	if ((currentNum % firstDivisor != 0) && (currentNum % secondDivisor != 0))
            		System.out.print(currentNum);
            	System.out.print(" ");
            }
            
            System.out.println();
        }
        
        buffer.close();
    }
}
