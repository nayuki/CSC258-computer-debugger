package csc258comp.runner;


@SuppressWarnings("serial")
public final class IllegalOpcodeException extends MachineException {
	
	public IllegalOpcodeException(String msg) {
		super(msg);
		if (msg == null)
			throw new NullPointerException();
	}
	
}
