package csc258comp.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import csc258comp.util.Utf8Reader;


/**
 * Represents a list of string lines, possibly associated with a file. Immutable.
 * @see Csc258Compiler
 */
public final class SourceCode implements Iterable<String> {
	
	public static SourceCode readFile(File file) throws IOException {
		if (file == null)
			throw new NullPointerException();
		
		List<String> lines = new ArrayList<String>();
		BufferedReader in = new Utf8Reader(file);
		try {
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				lines.add(line);
			}
		} finally {
			in.close();
		}
		return new SourceCode(file, lines);
	}
	
	
	
	private File file;
	
	private List<String> lines;
	
	
	
	public SourceCode(List<String> lines) {
		this(null, lines);
	}
	
	
	public SourceCode(File file, List<String> lines) {
		if (lines == null)
			throw new NullPointerException();
		this.file = file;
		this.lines = Collections.unmodifiableList(new ArrayList<String>(lines));
	}
	
	
	
	public File getFile() {
		return file;
	}
	
	
	public int getLineCount() {
		return lines.size();
	}
	
	
	public String getLineAt(int row) {
		return lines.get(row);
	}
	
	
	public Iterator<String> iterator() {
		return lines.iterator();
	}
	
	
	public String toString() {
		if (file != null)
			return String.format("%s (%d lines)", file.getPath(), lines.size());
		else
			return String.format("%s (%d lines)", super.toString(), lines.size());
	}
	
}
