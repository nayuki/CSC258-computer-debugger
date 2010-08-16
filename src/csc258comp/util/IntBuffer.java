package csc258comp.util;


public final class IntBuffer {
	
	private int[] values;
	
	private int length;
	
	
	
	public IntBuffer() {
		values = new int[256];
	}
	
	
	
	public int length() {
		return length;
	}
	
	
	public int get(int index) {
		if (index < 0 || index >= length)
			throw new IndexOutOfBoundsException();
		return values[index];
	}
	
	
	public int set(int index, int value) {
		if (index < 0 || index >= length)
			throw new IndexOutOfBoundsException();
		return values[index] = value;
	}
	
	
	public void append(int value) {
		while (length + 1 > values.length)
			resize(values.length * 2);
		values[length] = value;
		length++;
	}
	
	
	public int[] toArray() {
		int[] result = new int[length];
		System.arraycopy(values, 0, result, 0, length);
		return result;
	}
	
	
	private void resize(int newLength) {
		int[] temp = new int[newLength];
		System.arraycopy(values, 0, temp, 0, Math.min(values.length, newLength));
		values = temp;
	}
	
}
