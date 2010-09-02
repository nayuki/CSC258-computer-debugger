package csc258comp.debugger;

import java.io.IOException;
import java.io.InputStream;

import csc258comp.util.ByteBuffer;


final class RememberingInputStream extends InputStream implements Cloneable {
	
	private InputStream input;
	
	private ByteBuffer memory;
	
	private int position;
	
	
	
	public RememberingInputStream(InputStream in) {
		if (in == null)
			throw new NullPointerException();
		input = in;
		memory = new ByteBuffer();
		position = 0;
	}
	
	
	
	@Override
	public int read() throws IOException {
		if (position < memory.length()) {
			position++;
			return memory.get(position - 1);
		} else {
			int result = input.read();
			if (result != -1) {
				memory.append((byte)result);
				position++;
			}
			return result;
		}
	}
	
	
	@Override
	public int available() throws IOException {
		int temp = input.available();
		if (temp < 0)
			throw new AssertionError();
		return memory.length() - position + temp;
	}
	
	
	@Override
	public void close() throws IOException {
		input.close();
	}
	
	
	@Override
	public RememberingInputStream clone() {
		try {
			return (RememberingInputStream)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
	
}
