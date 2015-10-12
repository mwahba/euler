package net.mwahba.library;

public class InsertionSort {
	
	public void insert(float[] array, float item) {
		int arrayLength = array.length;
		boolean inserted = false;
		float temp = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < arrayLength; i++) {
			if (inserted) {
				float valToShift = array[i];
				array[i] = temp;
				temp = valToShift;
			} else if (array[i] > item) {
				temp = array[i];
				array[i] = item;
				inserted = true;
			}
		}
	}
	
	public void insert(Object[] array, Object item) {
		
	}

}
