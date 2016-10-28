/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;

import java.util.Arrays;

import csc258comp.runner.Machine;


final class CopyOnWriteMemory implements Cloneable {
	
	private int[][] values;
	
	private boolean[] readOnly;
	
	
	
	public CopyOnWriteMemory() {
		values = new int[1 << 12][];
		Arrays.fill(values, new int[1 << 12]);
		
		readOnly = new boolean[1 << 12];
		Arrays.fill(readOnly, true);
	}
	
	
	
	public int get(int addr) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		
		int a0 = (addr >>> 12) & ((1 << 12) - 1);
		int a1 = (addr >>>  0) & ((1 << 12) - 1);
		
		return values[a0][a1];
	}
	
	
	public void set(int addr, int val) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		
		int a0 = (addr >>> 12) & ((1 << 12) - 1);
		int a1 = (addr >>>  0) & ((1 << 12) - 1);
		
		if (readOnly[a0]) {
			values[a0] = values[a0].clone();
			readOnly[a0] = false;
		}
		
		values[a0][a1] = val;
	}
	
	
	@Override
	public CopyOnWriteMemory clone() {
		try {
			Arrays.fill(readOnly, true);
			
			CopyOnWriteMemory copy = (CopyOnWriteMemory)super.clone();
			copy.values = copy.values.clone();
			copy.readOnly = copy.readOnly.clone();
			return copy;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
	
}
