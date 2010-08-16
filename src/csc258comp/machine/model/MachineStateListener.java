package csc258comp.machine.model;

import csc258comp.compiler.Program;


public interface MachineStateListener {
	
	public void haltedChanged(Machine m);
	
	public void programCounterChanged(Machine m);
	
	public void accumulatorChanged(Machine m);
	
	public void conditionCodeChanged(Machine m);
	
	public void memoryChanged(Machine m, int addr);
	
	public void programLoaded(Machine m, Program p);
	
}
