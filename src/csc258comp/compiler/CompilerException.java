package csc258comp.compiler;

import java.util.SortedMap;


@SuppressWarnings("serial")
public final class CompilerException extends RuntimeException {
	
	private SortedMap<Integer,String> errorMessages;
	
	private SourceCode sourceCode;
	
	
	
	public CompilerException(String msg) {
		super(msg);
		if (msg == null)
			throw new NullPointerException();
	}
	
	
	public CompilerException(String msg, SortedMap<Integer,String> errorMessages, SourceCode sourceCode) {
		super(msg);
		if (msg == null || errorMessages == null || sourceCode == null)
			throw new NullPointerException();
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
