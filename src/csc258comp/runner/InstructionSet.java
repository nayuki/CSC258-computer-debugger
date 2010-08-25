package csc258comp.runner;

import java.util.HashMap;
import java.util.Map;


/**
 * Contains information about the machine's instruction set and their mnemonics.
 * @see Executor
 */
public final class InstructionSet {
	
	private static Map<Integer,String> opcodeToMnemonic;
	
	private static Map<String,Integer> mnemonicToOpcode;
	
	
	static {
		opcodeToMnemonic = new HashMap<Integer,String>();
		mnemonicToOpcode = new HashMap<String,Integer>();
		
		addInstruction(0x00, "LDA");
		addInstruction(0x01, "STA");
		addInstruction(0x02, "ADD");
		addInstruction(0x03, "SUB");
		addInstruction(0x04, "MUL");
		addInstruction(0x05, "DIV");
		addInstruction(0x06, "MOD");
		addInstruction(0x07, "FLA");
		addInstruction(0x08, "FLS");
		addInstruction(0x09, "FLM");
		addInstruction(0x0A, "FLD");
		addInstruction(0x0B, "CIF");
		addInstruction(0x0C, "CFI");
		addInstruction(0x0D, "AND");
		addInstruction(0x0E, "IOR");
		addInstruction(0x0F, "XOR");
		addInstruction(0x10, "BUN");
		addInstruction(0x11, "BZE");
		addInstruction(0x12, "BSA");
		addInstruction(0x13, "BIN");
		addInstruction(0x14, "INP");
		addInstruction(0x15, "OUT");
	}
	
	
	private static void addInstruction(int opcode, String mnemonic) {
		if (!opcodeToMnemonic.containsKey(opcode))
			opcodeToMnemonic.put(opcode, mnemonic);
		
		if (mnemonicToOpcode.put(mnemonic, opcode) != null)
			throw new AssertionError("Attempting to map a mnemonic to multiple opcodes");
	}
	
	
	
	/**
	 * Returns the mnemonic for the specified operation code, or {@code null} if the opcode does not map to a mnemonic.
	 * @param opcode the specified opcode to translate to an mnemonic
	 * @return the mnemonic associated with the opcode, or {@code null} if none exist
	 */
	public static String getMnemonic(int opcode) {
		if ((opcode & 0xFF) != opcode)
			throw new IllegalArgumentException();
		
		if (opcodeToMnemonic.containsKey(opcode))
			return opcodeToMnemonic.get(opcode);
		else
			return null;
	}
	
	
	/**
	 * Returns the specified operation code for the specified mnemonic, or {@code -1} if the mnemonic does not map to an opcode.
	 * @param mnemonic the specified mnemonic to translate to an opcode
	 * @return the opcode associated with the mnemonic, or {@code -1} if none exist
	 */
	public static int getOpcode(String mnemonic) {
		if (mnemonicToOpcode.containsKey(mnemonic))
			return mnemonicToOpcode.get(mnemonic);
		else
			return -1;
	}
	
	
	
	/**
	 * Not instantiable.
	 */
	private InstructionSet() {}
	
}
