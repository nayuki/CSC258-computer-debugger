package csc258comp.runner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public final class BasicMachine implements Machine {
	
	private boolean isHalted;
	
	private int programCounter;
	
	private int accumulator;
	
	private boolean conditionCode;
	
	private int[] memory;
	
	private InputStream input;
	
	private OutputStream output;
	
	
	
	public BasicMachine(InputStream in, OutputStream out) {
		if (in == null || out == null)
			throw new NullPointerException();
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
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
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
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		if (addr >= memory.length)
			throw new RuntimeException("Full memory space currently not supported");
		return memory[addr];
	}
	
	
	@Override
	public void setMemoryAt(int addr, int val) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		if (addr >= memory.length)
			throw new RuntimeException("Full memory space currently not supported");
		memory[addr] = val;
	}
	
	
	@Override
	public int input() throws IOException {
		if (input.available() == 0) {
			try {
				Thread.sleep(0, 100000);
			} catch (InterruptedException e) {}
			return -1;
		} else
			return input.read();
	}
	
	
	@Override
	public boolean output(int b) throws IOException {
		output.write(b);
		output.flush();
		return true;
	}
	
}
