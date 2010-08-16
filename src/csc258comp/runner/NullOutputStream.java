package csc258comp.runner;

import java.io.IOException;
import java.io.OutputStream;


public final class NullOutputStream extends OutputStream {
	
	private final boolean throwException;
	
	
	
	public NullOutputStream() {
		this(false);
	}
	
	
	public NullOutputStream(boolean throwException) {
		this.throwException = throwException;
	}
	
	
	
	@Override
	public void write(int b) throws IOException {
		if (throwException)
			throw new RuntimeException("Attempted to read from null input stream");
	}
	
}
