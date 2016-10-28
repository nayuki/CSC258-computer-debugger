/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.runner;


@SuppressWarnings("serial")
public final class IllegalOpcodeException extends MachineException {
	
	public IllegalOpcodeException(String msg) {
		super(msg);
		if (msg == null)
			throw new NullPointerException();
	}
	
}
