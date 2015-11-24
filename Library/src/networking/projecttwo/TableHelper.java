package networking.projecttwo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * /**
 * CSE 5461 - Mark Wahba (wahba.2@osu.edu) - Programming Assignment 2
 * TableHelper - provide properly spaced output.
 * Please read README for more information.
 * 
 * Code based on TableBuilder code available from ksmpartners.com, appears in article authored by Michael Schaeffer (8/6/2013)
 * http://www.ksmpartners.com/2013/08/nicely-formatted-tabular-output-in-java/
 * 
 * @author Mark Wahba (wahba.2@osu.edu)
 */
public class TableHelper {
    List<String[]> rows = new LinkedList<String[]>();
 
    public void addRow(String... cols) {
    	rows.add(cols);
    }
 
    private int[] colWidths()
    {
        int cols = -1;
 
        for(String[] row : rows)
            cols = Math.max(cols, row.length);
 
        int[] widths = new int[cols];
 
        for(String[] row : rows) {
            for(int colNum = 0; colNum < row.length; colNum++) {
                widths[colNum] =
                    Math.max(
                        widths[colNum],
                        row[colNum].length());
            }
        }
 
        return widths;
    }
    
    public String rightPad(String currentString, int colWidth) {
    	while (currentString.length() < colWidth) {
    		currentString += " ";
    	}
    	
    	return currentString;
    }
 
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
 
        int[] colWidths = colWidths();
 
        Iterator<String[]> rowIterator = rows.iterator();
        
        while (rowIterator.hasNext()) {
        	String[] row = rowIterator.next();
        	for(int colNum = 0; colNum < row.length; colNum++) {
                buf.append(
                    rightPad(row[colNum], colWidths[colNum]));
                buf.append(' ');
            }
        	
        	if (rowIterator.hasNext()) {
        		buf.append('\r').append('\n');
        	}
        }
 
        return buf.toString();
    }
 
}
