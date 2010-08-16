package csc258comp.debugger;

import csc258comp.compiler.Program;
import csc258comp.runner.Machine;


public interface MachineStateListener {
	
	public void haltedChanged(Machine m);
	
	public void programCounterChanged(Machine m);
	
	public void accumulatorChanged(Machine m);
	
	public void conditionCodeChanged(Machine m);
	
	public void memoryChanged(Machine m, int addr);
	
	public void programLoaded(Machine m, Program p);
	
}
