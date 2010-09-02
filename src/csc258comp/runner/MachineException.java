package csc258comp.runner;


@SuppressWarnings("serial")
public class MachineException extends RuntimeException {
	
	public MachineException(String msg) {
		super(msg);
		if (msg == null)
			throw new NullPointerException();
	}
	
	
	public MachineException(String msg, Throwable cause) {
		super(msg, cause);
		if (msg == null || cause == null)
			throw new NullPointerException();
	}
	
}
