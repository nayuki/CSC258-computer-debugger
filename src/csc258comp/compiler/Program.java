package csc258comp.compiler;

import java.util.Map;


public class Program {
	
	public static final int OPSYS_ADDRESS = 0x31337;
	
	
	private static String[] OPCODES = {
		"LDA", "STA",
		"ADD", "SUB", "MUL", "DIV", "MOD",
		"FLA", "FLS", "FLM", "FLD",
		"CIF", "CFI",
		"AND", "IOR", "XOR",
		"BUN", "BZE", "BSA", "BIN",
		"INP", "OUT"
	};
	
	
	
	private int[] image;
	
	private int mainAddress;
	
	private Map<Integer,String> imageSourceCode;
	
	
	
	public Program(int[] image, int mainAddress, Map<Integer,String> imageSourceCode) {
		this.image = image.clone();
		this.mainAddress = mainAddress;
		this.imageSourceCode = imageSourceCode;
	}
	
	
	
	public int getImageSize() {
		return image.length;
	}
	
	
	public int[] getImage() {
		return image.clone();
	}
	
	
	public int getMainAddress() {
		return mainAddress;
	}
	
	
	public String getSourceLine(int index) {
		return imageSourceCode.get(index);
	}
	
	
	
	public static String getOpcodeName(int opcode) {
		if (opcode < 0)
			throw new IllegalArgumentException();
		if (opcode >= OPCODES.length)
			return null;
		return OPCODES[opcode];
	}
	
	
	public static int getOpcodeIndex(String opcode) {
		// Uses linear search, which is good enough for parsing small programs
		for (int i = 0; i < OPCODES.length; i++) {
			if (opcode.equals(OPCODES[i]))
				return i;
		}
		return -1;
	}
	
}
