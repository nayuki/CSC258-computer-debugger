/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;

import java.io.IOException;
import java.io.OutputStream;


final class CountingOutputStream extends OutputStream {
	
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
	public void flush() throws IOException {
		output.flush();
	}
	
	
	@Override
	public void close() throws IOException {
		output.close();
	}
	
	
	public long getCount() {
		return count;
	}
	
}
