package csc258comp.compiler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
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
	
	
	
	private static Pattern LABEL_REGEX = Pattern.compile("^([A-Za-z0-9_]+):\\s*");
	
	
	private IntBuffer image = new IntBuffer();
	
	private Map<String,Integer> labels = new HashMap<String,Integer>();
	private Map<Integer,String> references = new HashMap<Integer,String>();
	
	private Map<Integer,Integer> srcLineToAddr = new HashMap<Integer,Integer>();
	private Map<Integer,Integer> addrToSrcLine = new HashMap<Integer,Integer>();
	
	private SortedMap<Integer,String> errorMessages = new TreeMap<Integer,String>();
	
	private Fragment result;
	
	
	
	private Csc258Compiler(SourceCode source) throws CompilationException {
		String imageLine = "";
		for (int i = 0; i < source.getLineCount(); i++) {
			String line = source.getLineAt(i);
			line = line.trim();
			if (line.equals(""))  // Skip blank lines
				continue;
			
			// Consume labels
			while (true) {
				Matcher m = LABEL_REGEX.matcher(line);
				if (!m.find())
					break;
				String label = m.group(1);
				if (!labels.containsKey(label))
					labels.put(label, image.length());
				else
					errorMessages.put(i, String.format("Duplicate label \"%s\"", label));
				line = line.substring(m.end());
				imageLine += label + ": ";
			}
			
			if (line.equals(""))
				continue;
			
			Queue<String> tokens = toQueue(line.split("\\s+"));
			
			// Instruction word
			if (InstructionSet.getOpcodeIndex(tokens.element()) != -1) {
				int word = InstructionSet.getOpcodeIndex(tokens.remove()) << 24;
				if (!tokens.isEmpty()) {
					references.put(image.length(), tokens.remove());
					imageLine += line;
					appendWord(word, i);
				} else {
					errorMessages.put(i, "Reference expected after opcode");
				}
			}
			
			// Data word
			else if (tokens.element().length() == 1 && "IFCBHAW".indexOf(tokens.element()) != -1) {
				String type = tokens.remove();
				if (!tokens.isEmpty()) {
					String value = tokens.remove();
					
					switch (type.charAt(0)) {
						case 'I':
							try {
								imageLine += line;
								appendWord(Integer.parseInt(value), i);
							} catch (NumberFormatException e) {
								errorMessages.put(i, "Invalid integer value");
							}
							break;
							
						case 'F':
							try {
								imageLine += line;
								appendWord(Float.floatToRawIntBits(Float.parseFloat(value)), i);
							} catch (NumberFormatException e) {
								errorMessages.put(i, "Invalid floating-point value");
							}
							break;
							
						case 'C':
							imageLine += line;
							appendWord(parseChars(value), i);
							break;
							
						case 'B':
							try {
								long binval = Long.parseLong(value, 2);
								if (binval >= 0 && binval <= 0xFFFFFFFFL) {
									imageLine += line;
									appendWord((int)binval, i);
								} else {
									errorMessages.put(i, "Binary value out of range");
								}
							} catch (NumberFormatException e) {
								errorMessages.put(i, "Invalid binary value");
							}
							break;
							
						case 'H':
							try {
								long hexval = Long.parseLong(value, 16);
								if (hexval >= 0 && hexval <= 0xFFFFFFFFL) {
									imageLine += line;
									appendWord((int)hexval, i);
								} else {
									errorMessages.put(i, "Hexadecimal value out of range");
								}
							} catch (NumberFormatException e) {
								errorMessages.put(i, "Invalid hexadecimal value");
							}
							break;
							
						case 'A':
							imageLine += line;
							references.put(image.length(), value);
							appendWord(0, i);
							break;
							
						case 'W':
							int length = Integer.parseInt(value);
							imageLine += line;
							if (length > 0) {
								appendWord(0, i);
								if (length > 1)
									image.append(new int[length - 1]);
							}
							break;
					}
				} else {
					errorMessages.put(i, "Value expected after data type");
				}
				
			} else {
				errorMessages.put(i, "Illegal instruction or data");
			}
			
			imageLine = "";
			// Ignore the rest of the tokens in the line, which are comments
		}
		
		if (errorMessages.size() > 0)
			throw new CompilationException(String.format("%d compiler errors", errorMessages.size()), errorMessages, source);
		
		result = new Fragment(image.toArray(), labels, references, source, srcLineToAddr, addrToSrcLine);
	}
	
	
	private void appendWord(int word, int srcLine) {
		if (srcLine != -1) {
			int addr = image.length();
			srcLineToAddr.put(srcLine, addr);
			addrToSrcLine.put(addr, srcLine);
		}
		image.append(word);
	}
	
	
	
	private static int parseChars(String chars) {
		if (chars.length() < 2 || chars.length() > 6 || !chars.startsWith("'") || !chars.endsWith("'"))
			throw new IllegalArgumentException("Invalid format");
		chars = chars.substring(1, chars.length() - 1);
		int result = 0;
		for (int i = 0; i < chars.length(); i++) {
			if (chars.charAt(i) >= 0x80)
				throw new IllegalArgumentException("Non-ASCII character");
			result = result << 8 | chars.charAt(i);
		}
		return result;
	}
	
	
	private static Queue<String> toQueue(String[] array) {
		Queue<String> result = new LinkedList<String>();
		for (String s : array)
			result.add(s);
		return result;
	}
	
}
