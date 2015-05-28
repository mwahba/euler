package easy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SetIntersection {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            SortedSet<Integer> list = new TreeSet<Integer>();
            String firstListString = line.split(";")[0], secondListString = line.split(";")[1];
            
            for (String num : firstListString.split(",")) {
            	for (String secondNum : secondListString.split(",")) {
            		if (num.equals(secondNum)) {
            			list.add(Integer.parseInt(num));
            		}
            	}
            }
            
            Iterator<Integer> numsIterator = list.iterator();
            
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
