/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.compiler;

import java.util.SortedMap;


/**
 * Thrown when {@link MyCompiler#compile(SourceCode)} encounters an error.
 */
@SuppressWarnings("serial")
public final class CompilerException extends RuntimeException {
	
	private SortedMap<Integer,String> errorMessages;
	
	private SourceCode sourceCode;
	
	
	
	/**
	 * Constructs a compiler exception with the specified message.
	 * @param msg the message
	 * @throws NullPointerException if {@code msg} is {@code null}
	 */
	public CompilerException(String msg) {
		super(msg);
		if (msg == null)
			throw new NullPointerException();
	}
	
	
	/**
	 * Constructs a compiler exception with the specified message and line-based error messages.
	 * @param msg the main error message
	 * @param errorMessages the line-based error messages
	 * @param sourceCode the source code for the line-based error messages
	 * @throws NullPointerException if any argument is {@code null}
	 */
	public CompilerException(String msg, SortedMap<Integer,String> errorMessages, SourceCode sourceCode) {
		super(msg);
		if (msg == null || errorMessages == null || sourceCode == null)
			throw new NullPointerException();
		this.errorMessages = errorMessages;
		this.sourceCode = sourceCode;
	}
	
	
	
	/**
	 * Returns the line-based error messages.
	 * @return the line-based error messages
	 */
	public SortedMap<Integer,String> getErrorMessages() {
		return errorMessages;
	}
	
	
	/**
	 * Returns the source code associated with the line-based error messages.
	 * @return the source code
	 */
	public SourceCode getSourceCode() {
		return sourceCode;
	}
	
}
