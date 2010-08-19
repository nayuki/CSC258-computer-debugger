package csc258comp.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import csc258comp.runner.InstructionSet;
import csc258comp.util.IntBuffer;


public final class Csc258Compiler {
	
	public static Fragment compile(SourceCode source) throws CompilationException {
		if (source == null)
			throw new NullPointerException();
		return new Csc258Compiler(source).result;
	}
	
	
	
	private IntBuffer image = new IntBuffer();
	
	private Map<String,Integer> labels = new HashMap<String,Integer>();
	private Map<Integer,String> references = new HashMap<Integer,String>();
	
	private Map<Integer,Integer> srcLineToAddr = new HashMap<Integer,Integer>();
	private Map<Integer,Integer> addrToSrcLine = new HashMap<Integer,Integer>();
	
	private SortedMap<Integer,String> errorMessages = new TreeMap<Integer,String>();
	
	private Fragment result;
	
	
	
	private Csc258Compiler(SourceCode source) throws CompilationException {
		// Loop over source code lines
		for (int i = 0; i < source.getLineCount(); i++) {
			Tokenizer t = new Tokenizer(source.getLineAt(i));
			
			// Consume labels
			processLabels(t, i);
			
			if (t.isEmpty())
				continue;
			
			String mnemonic = t.nextMnemonic();
			if (mnemonic == null) {
				errorMessages.put(i, "Invalid character");
				continue;
			}
			mnemonic = mnemonic.toUpperCase();
			
			if (InstructionSet.getOpcodeIndex(mnemonic) != -1)  // Instruction word
				processInstructionWord(t, mnemonic, i);
			else if (mnemonic.length() == 1 && "IFCBHAW".indexOf(mnemonic) != -1)  // Data word
				processDataWord(t, mnemonic.charAt(0), i);
			else  // Invalid mnemonic
				errorMessages.put(i, String.format("Invalid mnemonic \"%s\"", mnemonic));
			
			// Ignore the rest of the line, which is treated as a comment
		}
		
		if (errorMessages.size() > 0)
			throw new CompilationException(String.format("%d compiler errors", errorMessages.size()), errorMessages, source);
		
		result = new Fragment(image.toArray(), labels, references, source, srcLineToAddr, addrToSrcLine);
	}


	private void processLabels(Tokenizer t, int lineNum) {
		while (true) {
			String label = t.nextLabel();
			if (label == null)
				break;
			
			if (!labels.containsKey(label))
				labels.put(label, image.length());
			else
				errorMessages.put(lineNum, String.format("Duplicate label \"%s\"", label));
		}
	}
	
	
	private void processInstructionWord(Tokenizer t, String mnemonic, int lineNum) {
		String ref = t.nextReference();
		if (ref == null) {
			errorMessages.put(lineNum, "Reference expected");
			return;
		}
		
		references.put(image.length(), ref);
		int word = InstructionSet.getOpcodeIndex(mnemonic) << 24;
		appendWord(word, lineNum);
	}
	
	
	private void processDataWord(Tokenizer t, char mnemonic, int lineNum) {
		String val = null;
		if ("IFBHW".indexOf(mnemonic) != -1) {
			val = t.nextToken();
			if (val == null) {
				errorMessages.put(lineNum, String.format("String expected", val));
				return;
			}
		}
		
		switch (mnemonic) {
			case 'I':
				try {
					appendWord(Integer.parseInt(val), lineNum);
				} catch (NumberFormatException e) {
					errorMessages.put(lineNum, String.format("Invalid integer value \"%s\"", val));
				}
				break;
				
			case 'F':
				try {
					appendWord(Float.floatToRawIntBits(Float.parseFloat(val)), lineNum);
				} catch (NumberFormatException e) {
					errorMessages.put(lineNum, String.format("Invalid floating-point value \"%s\"", val));
				}
				break;
				
			case 'C':
				val = t.nextString();
				if (val == null) {
					errorMessages.put(lineNum, "String expected");
					return;
				}
				try {
					appendWord(parseChars(val), lineNum);
				} catch (IllegalArgumentException e) {
					errorMessages.put(lineNum, e.getMessage());
				}
				
				break;
				
			case 'B':
				try {
					long binval = Long.parseLong(val, 2);
					if (val.charAt(0) != '-' && binval >= 0 && binval <= 0xFFFFFFFFL) {
						appendWord((int)binval, lineNum);
					} else {
						errorMessages.put(lineNum, "Binary value out of range");
					}
				} catch (NumberFormatException e) {
					errorMessages.put(lineNum, "Invalid binary value");
				}
				break;
				
			case 'H':
				try {
					long hexval = Long.parseLong(val, 16);
					if (val.charAt(0) != '-' && hexval >= 0 && hexval <= 0xFFFFFFFFL) {
						appendWord((int)hexval, lineNum);
					} else {
						errorMessages.put(lineNum, "Hexadecimal value out of range");
					}
				} catch (NumberFormatException e) {
					errorMessages.put(lineNum, "Invalid hexadecimal value");
				}
				break;
				
			case 'A':
				val = t.nextReference();
				if (val == null) {
					errorMessages.put(lineNum, "Reference expected");
					return;
				}
				references.put(image.length(), val);
				appendWord(0, lineNum);
				break;
				
			case 'W':
				try {
					int length = Integer.parseInt(val);
					if (length >= 0 && length <= (1 << 24)) {
						if (length > 0) {
							appendWord(0, lineNum);
							if (length > 1)
								image.append(new int[length - 1]);
						}
					} else if (length < 0) {
						errorMessages.put(lineNum, "Negative size");
					} else {
						errorMessages.put(lineNum, "Size out of range");
					}
				} catch (NumberFormatException e) {
					errorMessages.put(lineNum, "Invalid size");
				}
				break;
			
			default:
				throw new AssertionError();
		}
	}
	
	
	private void appendWord(int word, int srcLine) {
		if (srcLine != -1) {
			int addr = image.length();
			srcLineToAddr.put(srcLine, addr);
			addrToSrcLine.put(addr, srcLine);
		}
		image.append(word);
	}
	
	
	private static int parseChars(String str) {
		int count = 0;
		int result = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= 0x80)
				throw new IllegalArgumentException(String.format("Non-ASCII character U+%X", c));
			if (c == '\\') {
				i++;
				if (i == str.length())
					throw new IllegalArgumentException("Invalid escape");
				c = str.charAt(i);
				switch (c) {
					case '0':  c = '\0';  break;
					case 'b':  c = '\b';  break;
					case 'n':  c = '\n';  break;
					case 'r':  c = '\r';  break;
					case 't':  c = '\t';  break;
					case '\\':  break;
					case '\'':  break;
					default:
						throw new IllegalArgumentException(String.format("Invalid escape '\\%c'", c));
				}
			}
			if (count == 4)
				throw new IllegalArgumentException(String.format("String '%s' too long", str));
			result = result << 8 | c;
			count++;
		}
		return result;
	}
	
	
	
	private static class Tokenizer {
		
		private static Pattern WHITESPACE = Pattern.compile("^[ \t]*");
		private static Pattern LABEL = Pattern.compile("^([A-Za-z0-9_]+):[ \t]*");
		private static Pattern MNEMONIC = Pattern.compile("^([A-Za-z0-9]+)[ \t]*");
		private static Pattern REFERENCE = Pattern.compile("^([A-Za-z0-9_]+)");
		private static Pattern TOKEN = Pattern.compile("^([^ \t]+)[ \t]*");
		private static Pattern STRING = Pattern.compile("^'(([^'\\\\]|\\\\.)*)'");
		
		
		private String line;
		
		
		public Tokenizer(String line) {
			// Trim leading white space
			Matcher m = WHITESPACE.matcher(line);
			if (!m.find())
				throw new AssertionError();
			line = line.substring(m.end());
			
			this.line = line;
		}
		
		
		public boolean isEmpty() {
			return line.length() == 0;
		}
		
		
		public String nextLabel() {
			Matcher m = LABEL.matcher(line);
			if (!m.find())
				return null;
			line = line.substring(m.end());
			return m.group(1);
		}
		
		
		public String nextMnemonic() {
			Matcher m = MNEMONIC.matcher(line);
			if (!m.find())
				return null;
			line = line.substring(m.end());
			return m.group(1);
		}
		
		
		public String nextReference() {
			Matcher m = REFERENCE.matcher(line);
			if (!m.find())
				return null;
			line = line.substring(m.end());
			return m.group(1);
		}
		
		
		public String nextToken() {
			Matcher m = TOKEN.matcher(line);
			if (!m.find())
				return null;
			line = line.substring(m.end());
			return m.group(1);
		}
		
		
		public String nextString() {
			Matcher m = STRING.matcher(line);
			if (!m.find())
				return null;
			line = line.substring(m.end());
			return m.group(1);
		}
		
	}
	
}
