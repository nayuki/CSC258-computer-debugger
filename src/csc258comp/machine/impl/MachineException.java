package csc258comp.machine.impl;


@SuppressWarnings("serial")
public class MachineException extends RuntimeException {
	
	public MachineException() {
		super();
	}
	
	
	public MachineException(String msg) {
		super(msg);
	}
	
	
	public MachineException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
