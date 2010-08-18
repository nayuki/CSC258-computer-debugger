package csc258comp.util;

import java.util.Arrays;


public final class IntBuffer {
	
	private int[] values;
	
	private int length;
	
	
	
	public IntBuffer() {
		this(1);
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
	
	
	public int set(int index, int val) {
		if (index < 0 || index >= length)
			throw new IndexOutOfBoundsException();
		return values[index] = val;
	}
	
	
	public void append(int val) {
		ensureCapacity(length + 1);
		values[length] = val;
		length++;
	}
	
	
	public void append(int[] vals) {
		if (vals == null)
			throw new NullPointerException();
		ensureCapacity(length + vals.length);
		System.arraycopy(vals, 0, values, length, vals.length);
		length += vals.length;
	}
	
	
	public int[] toArray() {
		return Arrays.copyOf(values, length);
	}
	
	
	public String toString() {
		return String.format("%s (length=%d)", super.toString(), length); 
	}
	
	
	private void ensureCapacity(int size) {
		while (values.length < size)
			values = Arrays.copyOf(values, values.length * 2);
	}
	
}
