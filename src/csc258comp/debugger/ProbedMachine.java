package csc258comp.debugger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import csc258comp.compiler.Program;
import csc258comp.runner.Machine;
import csc258comp.runner.SimpleMachine;


public final class ProbedMachine implements Machine {
	
	private Machine machine;
	
	private Set<MachineStateListener> listeners;
	
	
	public ProbedMachine(InputStream in, OutputStream out) {
		machine = new SimpleMachine(in, out);
		listeners = new HashSet<MachineStateListener>();
	}
	
	
	
	@Override
	public boolean isHalted() {
		return machine.isHalted();
	}
	
	
	@Override
	public void setHalted(boolean halted) {
		machine.setHalted(halted);
		for (MachineStateListener listener : listeners)
			listener.haltedChanged(this);
	}
	
	
	@Override
	public int getProgramCounter() {
		return machine.getProgramCounter();
	}
	
	
	@Override
	public void setProgramCounter(int addr) {
		machine.setProgramCounter(addr);
		for (MachineStateListener listener : listeners)
			listener.programCounterChanged(this);
	}
	
	
	@Override
	public int getAccumulator() {
		return machine.getAccumulator();
	}
	
	
	@Override
	public void setAccumulator(int val) {
		machine.setAccumulator(val);
		for (MachineStateListener listener : listeners)
			listener.accumulatorChanged(this);
	}
	
	
	@Override
	public boolean getConditionCode() {
		return machine.getConditionCode();
	}
	
	
	@Override
	public void setConditionCode(boolean val) {
		machine.setConditionCode(val);
		for (MachineStateListener listener : listeners)
			listener.conditionCodeChanged(this);
	}
	
	
	@Override
	public int getMemoryAt(int addr) {
		return machine.getMemoryAt(addr);
	}
	
	
	@Override
	public void setMemoryAt(int addr, int val) {
		machine.setMemoryAt(addr, val);
		for (MachineStateListener listener : listeners)
			listener.memoryChanged(this, addr);
	}
	
	
	@Override
	public int input() throws IOException {
		return machine.input();
	}
	
	
	@Override
	public boolean output(int b) throws IOException {
		return machine.output(b);
	}
	
	
	public void loadProgram(Program prog) {
		int[] image = prog.getImage();
		setHalted(false);
		setProgramCounter(prog.getMainAddress());
		setAccumulator(0);
		setConditionCode(false);
		for (int i = 0; i < image.length; i++)
			setMemoryAt(i, image[i]);
		for (MachineStateListener listener : listeners)
			listener.programLoaded(this, prog);
	}
	
	
	public void addListener(MachineStateListener listener) {
		listeners.add(listener);
	}
	
	
	public void removeListener(MachineStateListener listener) {
		listeners.remove(listener);
	}
	
}
