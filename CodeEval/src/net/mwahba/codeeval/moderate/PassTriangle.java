package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PassTriangle {
	public static void main(String[] args) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(new File(args[0])));
        String line;
        
        List<List<Integer[]>> tree = new ArrayList<List<Integer[]>>();
        
        while ((line = buffer.readLine()) != null) {
        	List<Integer[]> row = new ArrayList<Integer[]>();
        	for (String num : line.trim().split(" ")) {
        		Integer[] currentRow = new Integer[2];
        		currentRow[0] = Integer.parseInt(num);
        		currentRow[1] = -1;
        		row.add(currentRow);
        	}
        	
        	tree.add(row);
        }
        
        System.out.println(findMax(tree, 0, 0));
        
        buffer.close();
	}

	private static int findMax(List<List<Integer[]>> tree, int row, int column) {
		if (tree.get(row).get(column)[1] > 0) {
			return tree.get(row).get(column)[0];
		}
		
		int currentMax = tree.get(row).get(column)[0];
		
		if (tree.size() - 1 != row) {
			int leftMax = findMax(tree, row + 1, column), 
					rightMax = findMax(tree, row + 1, column + 1);
			
			if (leftMax > rightMax) {
				currentMax += leftMax;
			} else {
				currentMax += rightMax;
			}
		}
		
		Integer[] currentRow = new Integer[2];
		currentRow[0] = currentMax;
		currentRow[1] = 1;
		tree.get(row).set(column, currentRow);
		
		return currentMax;
	}
}
