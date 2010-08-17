package csc258comp.compiler;

import java.util.SortedMap;


@SuppressWarnings("serial")
public final class CompilationException extends Exception {
	
	private SortedMap<Integer,String> errorMessages;
	
	private SourceCode sourceCode;
	
	
	
	public CompilationException() {
		super();
	}
	
	
	public CompilationException(String message) {
		super(message);
	}
	
	
	public CompilationException(String message, SortedMap<Integer,String> errorMessages, SourceCode sourceCode) {
		super(message);
		this.errorMessages = errorMessages;
		this.sourceCode = sourceCode;
	}
	
	
	
	public SortedMap<Integer,String> getErrorMessages() {
		return errorMessages;
	}
	
	
	public SourceCode getSourceCode() {
		return sourceCode;
	}
	
}
