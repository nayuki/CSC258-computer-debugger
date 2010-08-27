package csc258comp.debugger;

import java.io.IOException;
import java.io.OutputStream;


class CountingOutputStream extends OutputStream {
	
	private final OutputStream output;
	
	private long count;
	
	
	
	public CountingOutputStream(OutputStream out) {
		output = out;
		count = 0;
	}
	
	
	
	@Override
	public void write(int b) throws IOException {
		output.write(b);
		count++;
	}
	
	
	@Override
	public void close() throws IOException {
		output.close();
	}
	
	
	public long getCount() {
		return count;
	}
	
}
