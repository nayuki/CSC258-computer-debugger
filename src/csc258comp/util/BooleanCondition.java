/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.util;


public final class BooleanCondition {
	
	private boolean value;
	
	
	
	public BooleanCondition(boolean val) {
		value = val;
	}
	
	
	
	public synchronized boolean getValue() {
		return value;
	}
	
	
	public synchronized void setValue(boolean val) {
		if (value != val) {
			value = val;
			notifyAll();
		}
	}
	
	
	public synchronized void waitForValue(boolean val) throws InterruptedException {
		while (value != val)
			wait();
	}
	
}
