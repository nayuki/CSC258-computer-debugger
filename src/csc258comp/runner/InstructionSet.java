package csc258comp.runner;


/**
 * Contains information about the machine's instruction set and their mnemonics.
 * @see Executor
 */
public final class InstructionSet {
	
	/**
	 * The list of mnemonics. Their element indices are directly used as opcodes.
	 */
	private static final String[] MNEMONICS = {
		"LDA", "STA",
		"ADD", "SUB", "MUL", "DIV", "MOD",
		"FLA", "FLS", "FLM", "FLD",
		"CIF", "CFI",
		"AND", "IOR", "XOR",
		"BUN", "BZE", "BSA", "BIN",
		"INP", "OUT"
	};
	
	
	
	/**
	 * Returns the mnemonic for the specified operation code, or {@code null} if the opcode does not map to a mnemonic.
	 * @param opcode the specified opcode to translate to an mnemonic
	 * @return the mnemonic associated with the opcode, or {@code null} if none exist
	 */
	public static String getOpcodeName(int opcode) {
		if (opcode < 0)
			throw new IllegalArgumentException();
		if (opcode >= MNEMONICS.length)
			return null;
		return MNEMONICS[opcode];
	}
	
	
	/**
	 * Returns the specified operation code for the specified mnemonic, or {@code -1} if the mnemonic does not map to an opcode.
	 * @param mnemonic the specified mnemonic to translate to an opcode
	 * @return the opcode associated with the mnemonic, or {@code -1} if none exist
	 */
	public static int getOpcodeIndex(String mnemonic) {
		if (mnemonic == null)
			throw new NullPointerException();
		// Uses linear search, which is good enough for parsing small programs
		for (int i = 0; i < MNEMONICS.length; i++) {
			if (mnemonic.equals(MNEMONICS[i]))
				return i;
		}
		return -1;
	}
	
	
	
	/**
	 * Not instantiable.
	 */
	private InstructionSet() {}
	
}
