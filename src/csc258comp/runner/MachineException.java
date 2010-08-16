package csc258comp.runner;


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
