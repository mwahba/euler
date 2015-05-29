package net.mwahba.codeeval.moderate;

import java.nio.ByteOrder;

public class SystemEndianness {

	public static void main(String[] args) {
		if(ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
			System.out.println("BigEndian");
		} else {
			System.out.println("LittleEndian");
		}
	}

}
