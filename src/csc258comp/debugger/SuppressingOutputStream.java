package csc258comp.debugger;

import java.io.IOException;
import java.io.OutputStream;


class SuppressingOutputStream extends OutputStream implements Cloneable {
	
	private final CountingOutputStream output;
	
	private long count;
	
	
	
	public SuppressingOutputStream(OutputStream out) {
		output = new CountingOutputStream(out);
		count = 0;
	}
	
	
	
	@Override
	public void write(int b) throws IOException {
		if (count > output.getCount())
			throw new AssertionError();
		
		if (count == output.getCount())
			output.write(b);
		count++;
	}
	
	
	@Override
	public void close() throws IOException {
		output.close();
	}
	
	
	public SuppressingOutputStream clone() {
		try {
			return (SuppressingOutputStream)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
	
}
