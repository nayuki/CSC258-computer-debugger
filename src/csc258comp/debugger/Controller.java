package csc258comp.debugger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import csc258comp.runner.Executor;
import csc258comp.runner.Machine;


final class Controller {
	
	private final DebugMachine initialMachine;
	
	private DebugMachine machine;
	
	private final Set<Integer> breakpoints;
	
	private long stepCount;
	
	private boolean isRunning;
	
	private volatile boolean suspendRequested;
	
	
	
	public Controller(DebugMachine m) {
		if (m == null)
			throw new NullPointerException();
		initialMachine = m.clone();
		machine = m;
		breakpoints = new HashSet<Integer>();
		stepCount = 0;
		isRunning = false;
		suspendRequested = false;
	}
	
	
	
	public Machine getMachine() {
		return machine;
	}
	
	
	public synchronized long getStepCount() {
		return stepCount;
	}
	
	
	public synchronized void addBreakpoint(int addr) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		breakpoints.add(addr);
	}
	
	
	public synchronized void removeBreakpoint(int addr) {
		if (addr < 0 || addr >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Address out of bounds");
		breakpoints.remove(addr);
	}
	
	
	public Set<Integer> getBreakpoints() {
		return Collections.unmodifiableSet(breakpoints);
	}
	
	
	public synchronized void step() {
		synchronized (machine) {
			if (!machine.isHalted()) {
				Executor.step(machine);
				stepCount++;
			}
		}
	}
	
	
	public void run() {
		synchronized (this) {
			if (isRunning)
				return;
			isRunning = true;
			suspendRequested = false;
		}
		
		while (!suspendRequested) {
			synchronized (this) {
				synchronized (machine) {
					if (machine.isHalted())
						break;
				}
				step();
				if (breakpoints.contains(machine.getProgramCounter()))
					break;
			}
		}
		
		synchronized (this) {
			isRunning = false;
			suspendRequested = false;
		}
	}
	
	
	public void suspend() {
		suspendRequested = true;
	}
	
	
	public synchronized void stepBack() {
		if (stepCount == 0)
			return;
		
		machine = initialMachine.clone();
		long count = stepCount - 1;
		stepCount = 0;
		for (long i = 0; i < count; i++)
			step();
	}
	
	
	public synchronized boolean isRunning() {
		return isRunning;
	}
	
}
