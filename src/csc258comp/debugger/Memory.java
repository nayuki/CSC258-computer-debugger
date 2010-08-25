package csc258comp.debugger;

import csc258comp.runner.Machine;


final class Memory {
	
	private int[][][] values;
	
	
	
	public Memory() {
		values = new int[1 << 8][][];
	}
	
	
	
	public int get(int addr) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		
		int a0 = (addr >>> 16) & ((1 << 8) - 1);
		int a1 = (addr >>>  8) & ((1 << 8) - 1);
		int a2 = (addr >>>  0) & ((1 << 8) - 1);
		
		int[][] v0 = values[a0];
		if (v0 == null)
			values[a0] = v0 = new int[1 << 8][];
		
		int[] v1 = v0[a1];
		if (v1 == null)
			v0[a1] = v1 = new int[1 << 8];
			
		return v1[a2];
	}
	
	
	public void set(int addr, int val) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		
		int a0 = (addr >>> 16) & ((1 << 8) - 1);
		int a1 = (addr >>>  8) & ((1 << 8) - 1);
		int a2 = (addr >>>  0) & ((1 << 8) - 1);
		
		int[][] v0 = values[a0];
		if (v0 == null)
			values[a0] = v0 = new int[1 << 8][];
		
		int[] v1 = v0[a1];
		if (v1 == null)
			v0[a1] = v1 = new int[1 << 8];
		
		v1[a2] = val;
	}
	
}
