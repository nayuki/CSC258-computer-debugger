package csc258comp.compiler;

import java.util.Map;


@SuppressWarnings("serial")
public class LinkerException extends RuntimeException {
	
	private Map<SourceLine,String> errorMessages;
	
	
	
	public LinkerException() {
		super();
	}
	
	
	public LinkerException(String msg) {
		super(msg);
		if (msg == null)
			throw new NullPointerException();
	}
	
	
	public LinkerException(String msg, Map<SourceLine,String> errorMessages) {
		super(msg);
		if (msg == null || errorMessages == null)
			throw new NullPointerException();
		this.errorMessages = errorMessages;
	}
	
	
	
	public Map<SourceLine,String> getErrorMessages() {
		return errorMessages;
	}
	
}
