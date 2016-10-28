/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.runner;

import java.io.IOException;
import java.io.InputStream;


public final class NullInputStream extends InputStream {
	
	private final boolean throwException;
	
	
	
	public NullInputStream() {
		this(false);
	}
	
	
	public NullInputStream(boolean throwException) {
		this.throwException = throwException;
	}
	
	
	
	@Override
	public int read() throws IOException {
		if (throwException)
			throw new RuntimeException("Attempted to read from null input stream");
		else
			return -1;
	}
	
	
	@Override
	public int available() {
		if (throwException)
			throw new RuntimeException("Attempted to read from null input stream");
		else
			return 0;
	}
	
}
