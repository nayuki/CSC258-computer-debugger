/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import csc258comp.runner.Executor;
import csc258comp.runner.Machine;


final class Controller {
	
	private final MachineSnapshot initialSnapshot;
	
	private MachineSnapshot recentSnapshot;
	
	private DebugMachine machine;
	
	private final Set<Integer> breakpoints;
	
	private long stepCount;
	
	private boolean isRunning;
	
	private volatile boolean suspendRequested;
	
	
	
	public Controller(DebugMachine m) {
		Objects.requireNonNull(m);
		initialSnapshot = new MachineSnapshot(m, 0);
		recentSnapshot = null;
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
				
				if (stepCount % 30000 == 0)
					recentSnapshot = new MachineSnapshot(machine, stepCount);
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
		else
			stepTo(stepCount - 1);
	}
	
	
	public synchronized boolean isRunning() {
		return isRunning;
	}
	
	
	private void stepTo(long steps) {
		if (steps < stepCount) {
			MachineSnapshot snap = findSnapshot(steps);
			long count = steps - snap.stepCount;
			restoreSnapshot(snap);
			for (long i = 0; i < count; i++)
				step();
		} else {
			while (steps < stepCount)
				step();
		}
	}
	
	
	private MachineSnapshot findSnapshot(long steps) {
		if (recentSnapshot != null && recentSnapshot.stepCount <= steps)
			return recentSnapshot;
		else
			return initialSnapshot;
	}
	
	
	private void restoreSnapshot(MachineSnapshot snap) {
		machine = snap.machine.clone();
		stepCount = snap.stepCount;
	}
	
}
