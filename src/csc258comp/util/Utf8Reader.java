package csc258comp.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public final class Utf8Reader extends BufferedReader {
	
	private final File file;
	
	private boolean initialLine;
	
	
	
	public Utf8Reader(File file) throws IOException {
		this(file, new BufferedInputStream(new FileInputStream(file)));
		initialLine = true;
	}
	
	
	public Utf8Reader(InputStream in) throws IOException {
		this(null, in);
	}
	
	
	private Utf8Reader(File file, InputStream in) throws IOException {
		this(file, new InputStreamReader(in, "UTF-8"));
	}
	
	
	public Utf8Reader(Reader in) {
		this(null, in);
	}
	
	
	private Utf8Reader(File file, Reader in) {
		super(in);
		this.file = file;
	}
	
	
	
	public String readLine() throws IOException {
		String line = super.readLine();
		if (initialLine && line != null && line.length() >= 1 && line.charAt(0) == '\uFEFF')
			line = line.substring(1, line.length());  // Strip BOM
		initialLine = false;
		return line;
	}
	
	
	public void close() throws IOException {
		super.close();
	}
	
	
	public String toString() {
		if (file != null)
			return super.toString() + ": " + file.getPath();
		else
			return super.toString();
	}
	
}
