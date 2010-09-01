package csc258comp.debugger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import csc258comp.runner.Loader;
import csc258comp.runner.Machine;
import csc258comp.runner.Program;


public final class DebugMachine implements Machine, Cloneable {
	
	private boolean isHalted;
	
	private int programCounter;
	
	private int accumulator;
	
	private boolean conditionCode;
	
	private Memory memory;
	
	private RememberingInputStream input;
	
	private SuppressingOutputStream output;
	
	
	
	public DebugMachine(InputStream in, OutputStream out) {
		if (in == null || out == null)
			throw new NullPointerException();
		isHalted = false;
		programCounter = 0;
		accumulator = 0;
		conditionCode = false;
		memory = new Memory();
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
	
	
	public void loadProgram(Program prog) {
		if (prog == null)
			throw new NullPointerException();
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
