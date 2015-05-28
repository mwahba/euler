package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LongestLines {
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        List<String> lines = new ArrayList<String>();
        Integer numLines = -1;
        
        while ((line = buffer.readLine()) != null) {
            if (numLines != -1) {
            	lines.add(line);
            } else {
            	numLines = Integer.parseInt(line);
            }
        }
        
        java.util.Collections.sort(lines, new Comparator<String>() {
        	@Override
        	public int compare(String first, String second) {
        		return second.length() - first.length();
        	}
        });
        
        while (numLines > 0) {
        	System.out.println(lines.remove(0));
        	numLines--;
        }
        
        buffer.close();
	}

}
