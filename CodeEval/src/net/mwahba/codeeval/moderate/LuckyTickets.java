package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class LuckyTickets {
	
	public static Map<Integer, Map<Integer, BigInteger>> results = new HashMap<Integer, Map<Integer, BigInteger>>();
	
	public static void main(String[] args) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(new File(args[0])));
        String line;
        
        while ((line = buffer.readLine()) != null) {
        	int numDigits = Integer.parseInt(line), numDigitsOneSide = numDigits / 2;
        	BigInteger result = new BigInteger("0");
        	
        	for (int currentNum = 0; currentNum <= numDigitsOneSide * 9; currentNum++) {
        		result = result.add(findNumWays(currentNum, numDigitsOneSide).pow(2));
        	}
        	
        	System.out.println(result);
        }
        
        buffer.close();
	}
	
	private static BigInteger findNumWays(int currentNum, int numDigitsOneSide) {
		Map<Integer, BigInteger> currentRow;
		if (results.containsKey(numDigitsOneSide)) {
			currentRow = results.get(numDigitsOneSide);
		} else {
			currentRow = new HashMap<Integer, BigInteger>();
		}
		
		if (currentRow.containsKey(currentNum)) {
			return currentRow.get(currentNum);
		} else {
			switch (numDigitsOneSide) {
			case 1:
				return new BigInteger("1");
			case 2:
				if (currentNum <= 9) {
					currentRow.put(currentNum, new BigInteger("" + (currentNum + 1)));
					results.put(numDigitsOneSide, currentRow);
					return new BigInteger("" + (currentNum + 1));
				} else {
					int result = Math.round((2 - ((float) currentNum / 9)) * 9) + 1;
					currentRow.put(currentNum, new BigInteger("" + result));
					results.put(numDigitsOneSide, currentRow);
					return new BigInteger("" + result);
				}
			default:
				int left = 0;
				BigInteger result = new BigInteger("0");
				if (currentNum > 9) {
					left = currentNum - 9;
					if (left < 0) {
						left = 0;
					}
				}
				
				if (results.containsKey(numDigitsOneSide) && results.get(numDigitsOneSide).containsKey(currentNum - 1)) {
					result = (BigInteger) results.get(numDigitsOneSide).get(currentNum - 1);
					
					if (results.get(numDigitsOneSide -1).containsKey(currentNum)) {
						result = result.add(findNumWays(currentNum, numDigitsOneSide -1));
					}
					
					if (currentNum > 9) {
						result = result.subtract(findNumWays(currentNum - 10, numDigitsOneSide - 1));
					}
				} else {
					while (left <= 9 * (numDigitsOneSide - 1) && left <= currentNum) {
						result = result.add(findNumWays(left, numDigitsOneSide - 1));
						left++;
					}
				}
				
				currentRow.put(currentNum, result);
				results.put(numDigitsOneSide, currentRow);
				return result;
			}
		}
	}
}
