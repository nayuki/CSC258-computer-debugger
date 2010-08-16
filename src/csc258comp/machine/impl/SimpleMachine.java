package csc258comp.machine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import csc258comp.machine.model.Machine;


public final class SimpleMachine implements Machine {
	
	private boolean isHalted;
	
	private int programCounter;
	
	private int accumulator;
	
	private boolean conditionCode;
	
	private int[] memory;
	
	private InputStream input;
	
	private OutputStream output;
	
	
	
	public SimpleMachine(InputStream in, OutputStream out) {
		isHalted = false;
		programCounter = 0;
		accumulator = 0;
		conditionCode = false;
		memory = new int[1 << 16];
		input = in;
		output = out;
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
		if (addr < 0 || addr >= (1 << 24))
			throw new IllegalArgumentException("Address out of bounds");
		programCounter = addr;
	}
	
	
	@Override
	public int getAccumulator() {
		return accumulator;
	}
	
	
	@Override
	public void setAccumulator(int val) {
		accumulator = val;
	}
	
	
	@Override
	public boolean getConditionCode() {
		return conditionCode;
	}
	
	
	@Override
	public void setConditionCode(boolean val) {
		conditionCode = val;
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
	public void setMemoryAt(int addr, int val) {
		if (addr < 0 || addr >= (1 << 24))
			throw new IllegalArgumentException("Address out of bounds");
		if (addr >= memory.length)
			throw new RuntimeException("Full memory space currently not supported");
		memory[addr] = val;
	}
	
	
	@Override
	public int input() throws IOException {
		if (input.available() == 0)
			return -1;
		else
			return input.read();
	}
	
	
	@Override
	public boolean output(int b) throws IOException {
		output.write(b);
		output.flush();
		return true;
	}
	
}
