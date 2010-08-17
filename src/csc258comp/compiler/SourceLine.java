package csc258comp.compiler;


public class SourceLine {
	
	public final SourceCode sourceCode;
	
	public final int lineNumber;
	
	
	
	public SourceLine(SourceCode source, int line) {
		if (source == null)
			throw new NullPointerException();
		if (line < 0 || line >= source.getLineCount())
			throw new IndexOutOfBoundsException();
		sourceCode = source;
		lineNumber = line;
	}
	
	
	
	public String getString() {
		return sourceCode.getLineAt(lineNumber);
	}
	
	
	public boolean equals(Object obj) {
		if (obj instanceof SourceLine) {
			SourceLine other = (SourceLine)obj;
			return sourceCode.equals(other.sourceCode)
			    && lineNumber == other.lineNumber;
		} else
			return false;
	}
	
	
	public int hashCode() {
		return sourceCode.hashCode() + lineNumber;
	}
	
}
