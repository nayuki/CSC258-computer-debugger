/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.runner;

import java.util.Objects;


@SuppressWarnings("serial")
public final class MachineIoException extends MachineException {
	
	public MachineIoException(String msg, Throwable cause) {
		super(msg, cause);
		Objects.requireNonNull(msg);
		Objects.requireNonNull(cause);
	}
	
}
