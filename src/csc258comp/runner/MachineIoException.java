package csc258comp.runner;


@SuppressWarnings("serial")
public final class MachineIoException extends MachineException {
	
	public MachineIoException(String msg, Throwable cause) {
		super(msg, cause);
		if (msg == null || cause == null)
			throw new NullPointerException();
	}
	
}
