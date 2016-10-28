/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

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
 * Represents a list of string lines, associated with a file (possibly null). Immutable.
 * @see MyCompiler
 */
public final class SourceCode implements Iterable<String> {
	
	/**
	 * Returns a source code object constructed from the lines of the contents of the specified file. The file associated with the source code is set to the specified file.
	 * @param file the file to read
	 * @return a source code object containing the lines of the file
	 * @throws IOException if an I/O exception occurs
	 */
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
	
	
	
	/**
	 * Constructs a source code from the specified string, which is split into a list of lines. The file is set to {@code null}.
	 * @param str the string representing the source code
	 */
	public SourceCode(String str) {
		String[] temp = str.split("(\r\n|\r|\n)");
		lines = new ArrayList<String>();
		Collections.addAll(lines, temp);
		file = null;
	}
	
	
	/**
	 * Constructs a source code from the specified list of lines. The file is set to {@code null}.
	 * @param lines the list of lines of source code
	 * @throws NullPointerException if {@code lines} is {@code null}
	 */
	public SourceCode(List<String> lines) {
		this(null, lines);
	}
	
	
	/**
	 * Constructs a source code from the specified list of lines and the specified file. The file can be {@code null}.
	 * @param file the file associated with the source code
	 * @param lines the list of lines of source code
	 * @throws NullPointerException if {@code lines} is {@code null}
	 */
	public SourceCode(File file, List<String> lines) {
		if (lines == null)
			throw new NullPointerException();
		this.file = file;
		this.lines = Collections.unmodifiableList(new ArrayList<String>(lines));
	}
	
	
	
	/**
	 * Returns the file associated with this source code, possibly {@code null}.
	 * @return the file associated with this source code
	 */
	public File getFile() {
		return file;
	}
	
	
	/**
	 * Returns the number of lines this source code has, a non-negative integer.
	 * @return the number of lines this source code has
	 */
	public int getLineCount() {
		return lines.size();
	}
	
	
	/**
	 * Returns the specified line of source code (0-based indexing).
	 * @param row the line to get (0-based)
	 * @return the specified line of source code
	 */
	public String getLineAt(int row) {
		return lines.get(row);
	}
	
	
	/**
	 * Returns a new iterator over the lines of source code
	 * @return a new iterator over the lines of source code
	 */
	@Override
	public Iterator<String> iterator() {
		return lines.iterator();
	}
	
	
	/**
	 * Returns a string representation of this source code object. The format is subject to change.
	 * @return a string representation of this source code object
	 */
	@Override
	public String toString() {
		if (file != null)
			return String.format("%s (%d lines)", file.getPath(), lines.size());
		else
			return String.format("%s (%d lines)", super.toString(), lines.size());
	}
	
}
