/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import csc258comp.runner.Loader;
import csc258comp.runner.Machine;
import csc258comp.runner.Program;


final class DebugMachine implements Machine, Cloneable {
	
	private boolean isHalted;
	
	private int programCounter;
	
	private int accumulator;
	
	private boolean conditionCode;
	
	private CopyOnWriteMemory memory;
	
	private RememberingInputStream input;
	
	private SuppressingOutputStream output;
	
	
	
	public DebugMachine(InputStream in, OutputStream out) {
		Objects.requireNonNull(in);
		Objects.requireNonNull(out);
		isHalted = false;
		programCounter = 0;
		accumulator = 0;
		conditionCode = false;
		memory = new CopyOnWriteMemory();
		input = new RememberingInputStream(in);
		output = new SuppressingOutputStream(out);
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
		return memory.get(addr);
	}
	
	
	@Override
	public void setMemoryAt(int addr, int val) {
		memory.set(addr, val);
	}
	
	
	@Override
	public int input() throws IOException {
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
	
	
	public void loadProgram(Program prog) {
		Objects.requireNonNull(prog);
		Loader.load(this, prog);
	}
	
	
	@Override
	public DebugMachine clone() {
		try {
			DebugMachine copy = (DebugMachine)super.clone();
			copy.memory = copy.memory.clone();
			copy.input = copy.input.clone();
			copy.output = copy.output.clone();
			return copy;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
	
}
