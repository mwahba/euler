package net.mwahba.codeeval.easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FibonacciPrintNth {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            int n = Integer.parseInt(line), first = 1, second = 1, third = 2, current = 3;
            if (n <= 3) {
            	switch (n) {
            	case 0:
            		System.out.println("0");
            		break;
            	case 1:
            	case 2:
            		System.out.println(first);
            		break;
            	case 3:
            		System.out.println(third);
            		break;
            	}
            } else {
            	while (current < n) {
            		first = second;
            		second = third;
            		third = first + second;
            		current++;
            	}
            	System.out.println(third);
            }
        }
        
        buffer.close();
	}

}
