package csc258comp.compiler;

import java.util.SortedMap;


@SuppressWarnings("serial")
public class LinkerException extends RuntimeException {
	
	private SortedMap<SourceLine,String> errorMessages;
	
	
	
	public LinkerException() {
		super();
	}
	
	
	public LinkerException(String message) {
		super(message);
	}
	
	
	public LinkerException(String message, SortedMap<SourceLine,String> errorMessages) {
		super(message);
		this.errorMessages = errorMessages;
	}
	
	
	
	public SortedMap<SourceLine,String> getErrorMessages() {
		return errorMessages;
	}
	
}
