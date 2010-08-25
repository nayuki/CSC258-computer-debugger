package csc258comp.debugger;

import csc258comp.runner.Machine;


public final class Memory {
	
	private int[] values;
	
	
	
	public Memory() {
		values = new int[Machine.ADDRESS_SPACE_SIZE];
	}
	
	
	
	public int get(int addr) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		return values[addr];
	}
	
	
	public void set(int addr, int val) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		values[addr] = val;
	}
	
}
