/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;


final class MachineSnapshot {
	
	public final DebugMachine machine;
	
	public final long stepCount;
	
	
	
	public MachineSnapshot(DebugMachine m, long steps) {
		machine = m.clone();
		stepCount = steps;
	}
	
}
