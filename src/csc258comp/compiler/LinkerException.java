/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.compiler;

import java.util.Map;


/**
 * Thrown when {@link Linker#link(Iterable)} encounters an error.
 */
@SuppressWarnings("serial")
public final class LinkerException extends RuntimeException {
	
	private Map<SourceLine,String> errorMessages;
	
	
	
	/**
	 * Constructs a linker exception with the specified message.
	 * @param msg the message
	 * @throws NullPointerException if {@code msg} is {@code null}
	 */
	public LinkerException(String msg) {
		super(msg);
		if (msg == null)
			throw new NullPointerException();
	}
	
	
	/**
	 * Constructs a linker exception with the specified message and line-based error messages.
	 * @param msg the main error message
	 * @param errorMessages the line-based error messages
	 * @throws NullPointerException if any argument is {@code null}
	 */
	public LinkerException(String msg, Map<SourceLine,String> errorMessages) {
		super(msg);
		if (msg == null || errorMessages == null)
			throw new NullPointerException();
		this.errorMessages = errorMessages;
	}
	
	
	
	/**
	 * Returns the line-based error messages.
	 * @return the line-based error messages
	 */
	public Map<SourceLine,String> getErrorMessages() {
		return errorMessages;
	}
	
}
