package csc258comp.runner;


/**
 * Contains information about the machine's instruction set.
 * @see Executor
 */
public final class InstructionSet {
	
	private static final String[] OPCODES = {
		"LDA", "STA",
		"ADD", "SUB", "MUL", "DIV", "MOD",
		"FLA", "FLS", "FLM", "FLD",
		"CIF", "CFI",
		"AND", "IOR", "XOR",
		"BUN", "BZE", "BSA", "BIN",
		"INP", "OUT"
	};
	
	
	
	public static String getOpcodeName(int opcode) {
		if (opcode < 0)
			throw new IllegalArgumentException();
		if (opcode >= OPCODES.length)
			return null;
		return OPCODES[opcode];
	}
	
	
	public static int getOpcodeIndex(String opcode) {
		if (opcode == null)
			throw new NullPointerException();
		// Uses linear search, which is good enough for parsing small programs
		for (int i = 0; i < OPCODES.length; i++) {
			if (opcode.equals(OPCODES[i]))
				return i;
		}
		return -1;
	}
	
	
	
	/**
	 * Not instantiable.
	 */
	private InstructionSet() {}
	
}
