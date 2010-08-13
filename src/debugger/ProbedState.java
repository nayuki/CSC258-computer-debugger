package debugger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import csc258comp.Executor;
import csc258comp.MachineState;
import csc258comp.MachineStateListener;
import csc258comp.Program;
import csc258comp.SimpleMachineState;


public final class ProbedState implements MachineState {
	
	private MachineState state;
	
	private Set<MachineStateListener> listeners;
	
	
	public ProbedState() {
		state = new SimpleMachineState();
		listeners = new HashSet<MachineStateListener>();
	}
	
	
	
	@Override
	public boolean getHalted() {
		return state.getHalted();
	}
	
	
	@Override
	public void setHalted(boolean halted) {
		state.setHalted(halted);
		for (MachineStateListener listener : listeners)
			listener.haltedChanged(this);
	}
	
	
	@Override
	public int getProgramCounter() {
		return state.getProgramCounter();
	}
	
	
	@Override
	public void setProgramCounter(int addr) {
		state.setProgramCounter(addr);
		for (MachineStateListener listener : listeners)
			listener.programCounterChanged(this);
	}
	
	
	@Override
	public int getAccumulator() {
		return state.getAccumulator();
	}
	
	
	@Override
	public void setAccumulator(int value) {
		state.setAccumulator(value);
		for (MachineStateListener listener : listeners)
			listener.accumulatorChanged(this);
	}
	
	
	@Override
	public boolean getConditionCode() {
		return state.getConditionCode();
	}
	
	
	@Override
	public void setConditionCode(boolean value) {
		state.setConditionCode(value);
		for (MachineStateListener listener : listeners)
			listener.conditionCodeChanged(this);
	}
	
	
	@Override
	public int getMemoryAt(int addr) {
		return state.getMemoryAt(addr);
	}
	
	
	@Override
	public void setMemoryAt(int addr, int value) {
		state.setMemoryAt(addr, value);
		for (MachineStateListener listener : listeners)
			listener.memoryChanged(this, addr);
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
	
	
	public void step(Executor e) throws IOException {
		e.step(this);
	}
	
}