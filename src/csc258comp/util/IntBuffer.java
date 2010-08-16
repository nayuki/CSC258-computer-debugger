package csc258comp.util;

import java.util.Arrays;


public final class IntBuffer {
	
	private int[] values;
	
	private int length;
	
	
	
	public IntBuffer() {
		this(256);
	}
	
	
	public IntBuffer(int initCapacity) {
		if (initCapacity <= 0)
			throw new IllegalArgumentException("Initial capacity must be positive");
		values = new int[initCapacity];
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
		return Arrays.copyOf(values, length);
	}
	
	
	private void resize(int newLength) {
		values = Arrays.copyOf(values, newLength);
	}
	
}
