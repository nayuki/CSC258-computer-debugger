/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.runner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;


public final class BasicMachine implements Machine {
	
	/**
	 * The amount of memory (in 32-bit words) that is allocated to a machine.
	 * This number can be defined as anything from 0 (inclusive) (pretty useless) to 2<sup>24</sup> (inclusive) (64 MiB).
	 * If memory outside of [0, {@code MEMORY_SIZE}) is read from or written to, then an {@link UnsupportedOperationException} is thrown.
	 */
	private static final int MEMORY_SIZE = 1 << 16;
	
	
	
	private boolean isHalted;
	
	private int programCounter;
	
	private int accumulator;
	
	private boolean conditionCode;
	
	private int[] memory;
	
	private InputStream input;
	
	private OutputStream output;
	
	
	
	public BasicMachine(InputStream in, OutputStream out) {
		Objects.requireNonNull(in);
		Objects.requireNonNull(out);
		isHalted = false;
		programCounter = 0;
		accumulator = 0;
		conditionCode = false;
		memory = new int[MEMORY_SIZE];
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
			throw new UnsupportedOperationException("Full memory space currently not supported");
		return memory[addr];
	}
	
	
	@Override
	public void setMemoryAt(int addr, int val) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		if (addr >= memory.length)
			throw new UnsupportedOperationException("Full memory space currently not supported");
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
		if ((b & 0xFF) != b)
			throw new IllegalArgumentException("Byte value out of range");
		output.write(b);
		output.flush();
		return true;
	}
	
}
