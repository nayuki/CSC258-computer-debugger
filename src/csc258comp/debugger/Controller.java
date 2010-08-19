package csc258comp.debugger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import csc258comp.runner.Executor;
import csc258comp.runner.Machine;


final class Controller {
	
	private final Machine machine;
	
	private Set<Integer> breakpoints;
	
	private long stepCount;
	
	
	
	public Controller(Machine m) {
		if (m == null)
			throw new NullPointerException();
		this.machine = m;
		breakpoints = new HashSet<Integer>();
		stepCount = 0;
	}
	
	
	
	public long getStepCount() {
		return stepCount;
	}
	
	
	public void addBreakpoint(int addr) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		breakpoints.add(addr);
	}
	
	
	public void removeBreakpoint(int addr) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		breakpoints.remove(addr);
	}
	
	
	public Set<Integer> getBreakpoints() {
		return Collections.unmodifiableSet(breakpoints);
	}
	
	
	public void step() {
		if (!machine.isHalted()) {
			Executor.step(machine);
			stepCount++;
		}
	}
	
	
	public void run() {
		while (!machine.isHalted()) {
			step();
			if (breakpoints.contains(machine.getProgramCounter()))
				break;
		}
	}
	
}
