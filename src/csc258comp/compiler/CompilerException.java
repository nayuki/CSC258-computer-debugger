package csc258comp.compiler;

import java.util.SortedMap;


@SuppressWarnings("serial")
public final class CompilerException extends Exception {
	
	private SortedMap<Integer,String> errorMessages;
	
	private SourceCode sourceCode;
	
	
	
	public CompilerException() {
		super();
	}
	
	
	public CompilerException(String message) {
		super(message);
	}
	
	
	public CompilerException(String message, SortedMap<Integer,String> errorMessages, SourceCode sourceCode) {
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
