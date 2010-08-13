package csc258comp.machine.impl;

import csc258comp.machine.model.MachineState;


public final class SimpleMachineState implements MachineState {
	
	private boolean isHalted;
	
	private int programCounter;
	
	private int accumulator;
	
	private boolean conditionCode;
	
	private int[] memory;
	
	
	
	public SimpleMachineState() {
		isHalted = false;
		programCounter = 0;
		accumulator = 0;
		conditionCode = false;
		memory = new int[1 << 16];
	}
	
	
	
	@Override
	public boolean isHalted() {
		return isHalted;
	}
	
	
	@Override
	public void setHalted(boolean halted) {
		this.isHalted = halted;
	}
	
	
	@Override
	public int getProgramCounter() {
		return programCounter;
	}
	
	
	@Override
	public void setProgramCounter(int addr) {
		programCounter = addr;
	}
	
	
	@Override
	public int getAccumulator() {
		return accumulator;
	}
	
	
	@Override
	public void setAccumulator(int value) {
		accumulator = value;
	}
	
	
	@Override
	public boolean getConditionCode() {
		return conditionCode;
	}
	
	
	@Override
	public void setConditionCode(boolean value) {
		conditionCode = value;
	}
	
	
	@Override
	public int getMemoryAt(int addr) {
		if (addr < 0 || addr >= (1 << 24))
			throw new IllegalArgumentException("Address out of bounds");
		if (addr >= memory.length)
			throw new RuntimeException("Full memory space currently not supported");
		return memory[addr];
	}
	
	
	@Override
	public void setMemoryAt(int addr, int value) {
		if (addr < 0 || addr >= (1 << 24))
			throw new IllegalArgumentException("Address out of bounds");
		if (addr >= memory.length)
			throw new RuntimeException("Full memory space currently not supported");
		memory[addr] = value;
	}
	
}
