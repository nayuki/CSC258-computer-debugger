package csc258comp.runner;


@SuppressWarnings("serial")
public final class IllegalOpcodeException extends MachineException {
	
	public IllegalOpcodeException() {
		super();
	}
	
	
	public IllegalOpcodeException(String msg) {
		super(msg);
	}
	
	
	public IllegalOpcodeException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
