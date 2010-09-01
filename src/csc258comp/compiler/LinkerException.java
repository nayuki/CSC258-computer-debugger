package csc258comp.compiler;

import java.util.Map;


@SuppressWarnings("serial")
public class LinkerException extends RuntimeException {
	
	private Map<SourceLine,String> errorMessages;
	
	
	
	public LinkerException() {
		super();
	}
	
	
	public LinkerException(String message) {
		super(message);
	}
	
	
	public LinkerException(String message, Map<SourceLine,String> errorMessages) {
		super(message);
		this.errorMessages = errorMessages;
	}
	
	
	
	public Map<SourceLine,String> getErrorMessages() {
		return errorMessages;
	}
	
}
