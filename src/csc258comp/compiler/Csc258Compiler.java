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
import csc258comp.runner.Program;



public final class Csc258Compiler {
	
	private static Pattern LABEL_REGEX = Pattern.compile("^([A-Za-z0-9_]+):\\s*");
	
	
	
	public static Program compile(SourceCode source) throws CompilationException {
		IntBuffer image = new IntBuffer();
		Map<Integer,String> imageSourceCode = new HashMap<Integer,String>();
		
		Map<String,Integer> labels = new HashMap<String,Integer>();
		Map<Integer,String> references = new HashMap<Integer,String>();
		Map<Integer,Integer> referenceSourceLines = new HashMap<Integer,Integer>();
		
		SortedMap<Integer,String> errorMessages = new TreeMap<Integer,String>();
		
		labels.put("opsys", Program.OPSYS_ADDRESS);
		
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
					referenceSourceLines.put(image.length(), i);
					imageLine += line;
					imageSourceCode.put(image.length(), imageLine);
					image.append(word);
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
								imageSourceCode.put(image.length(), imageLine);
								image.append(Integer.parseInt(value));
							} catch (NumberFormatException e) {
								errorMessages.put(i, "Invalid integer value");
							}
							break;
							
						case 'F':
							try {
								imageLine += line;
								imageSourceCode.put(image.length(), imageLine);
								image.append(Float.floatToRawIntBits(Float.parseFloat(value)));
							} catch (NumberFormatException e) {
								errorMessages.put(i, "Invalid floating-point value");
							}
							break;
							
						case 'C':
							imageLine += line;
							imageSourceCode.put(image.length(), imageLine);
							image.append(parseChars(value));
							break;
							
						case 'B':
							try {
								long binval = Long.parseLong(value, 2);
								if (binval >= 0 && binval <= 0xFFFFFFFFL) {
									imageLine += line;
									imageSourceCode.put(image.length(), imageLine);
									image.append((int)binval);
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
									imageSourceCode.put(image.length(), imageLine);
									image.append((int)hexval);
								} else {
									errorMessages.put(i, "Hexadecimal value out of range");
								}
							} catch (NumberFormatException e) {
								errorMessages.put(i, "Invalid hexadecimal value");
							}
							break;
							
						case 'A':
							imageLine += line;
							imageSourceCode.put(image.length(), imageLine);
							references.put(image.length(), value);
							referenceSourceLines.put(image.length(), i);
							image.append(0);
							break;
							
						case 'W':
							int length = Integer.parseInt(value);
							imageLine += line;
							if (length > 0)
								imageSourceCode.put(image.length(), imageLine);
							for (int j = 0; j < length; j++)
								image.append(0);
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
		
		for (int index : references.keySet()) {
			String label = references.get(index);
			if (labels.containsKey(label))
				image.set(index, image.get(index) | labels.get(label));
			else
				errorMessages.put(referenceSourceLines.get(index), String.format("Label \"%s\" not defined", label));
			
		}
		
		if (errorMessages.size() == 0)
			return new Program(image.toArray(), labels.get("main"), imageSourceCode);
		else
			throw new CompilationException(String.format("%d compiler errors", errorMessages.size()), errorMessages, source);
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
