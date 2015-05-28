package net.mwahba.codeeval.moderate;

import java.util.Comparator;

public class LineLengthComparator implements Comparator<String> {

	@Override
	public int compare(String firstLine, String secondLine) {
		return firstLine.length() - secondLine.length();
	}

}
