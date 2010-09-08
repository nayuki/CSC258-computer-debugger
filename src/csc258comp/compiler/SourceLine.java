package csc258comp.compiler;


/**
 * Represents a line of source code. Immutable.
 */
public final class SourceLine {
	
	public final SourceCode sourceCode;
	
	public final int lineNumber;
	
	
	
	/**
	 * Constructs a source line from the specified source code and line number (0-based indexing).
	 * @param source the source code
	 * @param line the line number (0-based)
	 */
	public SourceLine(SourceCode source, int line) {
		if (source == null)
			throw new NullPointerException();
		if (line < 0 || line >= source.getLineCount())
			throw new IndexOutOfBoundsException();
		sourceCode = source;
		lineNumber = line;
	}
	
	
	
	/**
	 * Returns the text of this source line. This is the text of the source code at the line number.
	 * @return the text of this source line
	 */
	public String getString() {
		return sourceCode.getLineAt(lineNumber);
	}
	
	
	/**
	 * Tests whether this source line is equal to the specified object. It is equal if and only if both objects are have equal source codes and equal line numbers.
	 * @return whether this source line is equal to the object 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SourceLine) {
			SourceLine other = (SourceLine)obj;
			return sourceCode.equals(other.sourceCode)
			    && lineNumber == other.lineNumber;
		} else
			return false;
	}
	
	
	/**
	 * Returns the hash code of this source line.
	 * @return the hash code of this source line
	 */
	@Override
	public int hashCode() {
		return sourceCode.hashCode() + lineNumber;
	}
	
	
	/**
	 * Returns a string representation of this source line. The format is subject to change.
	 * @return a string representation of this source line
	 */
	@Override
	public String toString() {
		if (sourceCode.getFile() != null)
			return String.format("%s:%d", sourceCode.getFile().getPath(), lineNumber);
		else
			return String.format("(%s):%d", sourceCode.toString(), lineNumber);
	}
	
}
