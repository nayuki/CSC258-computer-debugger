/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.runner;

import java.util.Objects;


@SuppressWarnings("serial")
public final class IllegalOpcodeException extends MachineException {
	
	public IllegalOpcodeException(String msg) {
		super(msg);
		Objects.requireNonNull(msg);
	}
	
}
