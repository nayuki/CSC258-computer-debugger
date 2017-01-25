/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.util;

import java.util.Arrays;
import java.util.Objects;


public final class ByteBuffer {
	
	private byte[] values;
	
	private int length;
	
	
	
	public ByteBuffer() {
		this(1);
	}
	
	
	public ByteBuffer(int initCapacity) {
		if (initCapacity <= 0)
			throw new IllegalArgumentException("Initial capacity must be positive");
		values = new byte[initCapacity];
	}
	
	
	
	public int length() {
		return length;
	}
	
	
	public int get(int index) {
		if (index < 0 || index >= length)
			throw new IndexOutOfBoundsException();
		return values[index];
	}
	
	
	public int set(int index, byte val) {
		if (index < 0 || index >= length)
			throw new IndexOutOfBoundsException();
		return values[index] = val;
	}
	
	
	public void append(byte val) {
		ensureCapacity(length + 1);
		values[length] = val;
		length++;
	}
	
	
	public void append(byte[] vals) {
		Objects.requireNonNull(vals);
		ensureCapacity(length + vals.length);
		System.arraycopy(vals, 0, values, length, vals.length);
		length += vals.length;
	}
	
	
	public byte[] toArray() {
		return Arrays.copyOf(values, length);
	}
	
	
	@Override
	public String toString() {
		return String.format("%s (length=%d)", super.toString(), length);
	}
	
	
	private void ensureCapacity(int size) {
		while (values.length < size)
			values = Arrays.copyOf(values, values.length * 2);
	}
	
}
