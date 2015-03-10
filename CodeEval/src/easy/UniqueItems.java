package easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UniqueItems {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            Set<Integer> uniqueNums = new HashSet<Integer>();
            for (String num : line.split(",")) {
            	uniqueNums.add(Integer.parseInt(num));
            }
            
            Iterator<Integer> numsIterator = uniqueNums.iterator();
            
            while (numsIterator.hasNext()) {
            	System.out.print(numsIterator.next());
            	if (numsIterator.hasNext()) {
            		System.out.print(",");
            	}
            }
            
            System.out.println();
        }
        
        buffer.close();
	}
}
