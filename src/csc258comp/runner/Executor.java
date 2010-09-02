package csc258comp.runner;

import java.io.IOException;


/**
 * Executes instructions on {@link Machine}s. This implements the behavior of instructions specified in {@link InstructionSet}.
 */
public final class Executor {
	
	public static final int OPSYS_ADDRESS = 0xFF0000;
	
	
	
	/**
	 * Executes one instruction on the specified machine.
	 * @param m the machine to execute on
	 * @throws MachineException if an invalid opcode is encountered or if an I/O exception occurs
	 */
	public static void step(Machine m) {
		if (m == null)
			throw new NullPointerException();
		
		// Do nothing if halted
		if (m.isHalted())
			return;
		
		// Check if halting
		final int pc = m.getProgramCounter();
		if (pc == OPSYS_ADDRESS) {
			m.setHalted(true);
			return;
		}
		
		// Fetch instruction
		final int instWord = m.getMemoryAt(pc);
		final int op = instWord >>> 24;
		final int memAddr = instWord & (Machine.ADDRESS_SPACE_SIZE - 1);  // The memory address embedded in the instruction word
		int nextPc = pc + 1;  // The default value is the address of the next word
		
		// Execute instruction
		if (op == 0) {  // LDA
			m.setAccumulator(m.getMemoryAt(memAddr));
			
		} else if (op == 1) {  // STA
			m.setMemoryAt(memAddr, m.getAccumulator());
			
		} else if (op >= 2 && op <= 6 || op >= 13 && op <= 15) {  // ADD, SUB, MUL, DIV, MOD, AND, IOR, XOR
			int x = m.getAccumulator();
			int y = m.getMemoryAt(memAddr);
			int result;
			boolean cc;
			switch (op) {
				case  2:  result = x + y;  cc = result != (long)x + y;  break;
				case  3:  result = x - y;  cc = result != (long)x - y;  break;
				case  4:  result = x * y;  cc = result != (long)x * y;  break;
				case  5:  result = (y != 0 ? x / y : x);  cc = y == 0;  break;
				case  6:  result = (y != 0 ? x % y : x);  cc = y == 0;  break;
				case 13:  result = x & y;  cc = result != 0;  break;
				case 14:  result = x | y;  cc = result != 0;  break;
				case 15:  result = x ^ y;  cc = result != 0;  break;
				default:  throw new AssertionError();
			}
			m.setAccumulator(result);
			m.setConditionCode(cc);
			
		} else if (op >= 7 && op <= 10) {  // FLA, FLS, FLM, FLD
			float x = Float.intBitsToFloat(m.getAccumulator());
			float y = Float.intBitsToFloat(m.getMemoryAt(memAddr));
			float result;
			switch (op) {
				case  7:  result = x + y;  break;
				case  8:  result = x - y;  break;
				case  9:  result = x * y;  break;
				case 10:  result = (y != 0 ? x / y : x);  break;
				default:  throw new AssertionError();
			}
			m.setAccumulator(Float.floatToRawIntBits(result));
			m.setConditionCode(Float.isInfinite(result) || op == 10 && y == 0);
			
		} else if (op == 11) {  // CIF
			int ival = m.getMemoryAt(memAddr);
			float fval = ival;  // Implicit cast to float32
			int bits = Float.floatToRawIntBits(fval);
			m.setAccumulator(bits);
			
		} else if (op == 12) {  // CFI
			int bits = m.getMemoryAt(memAddr);
			float fval = Float.intBitsToFloat(bits);
			int ival = Math.round(fval);
			m.setAccumulator(ival);
			
		} else if (op == 16) {  // BUN
			nextPc = memAddr;
			
		} else if (op == 17) {  // BZE
			if (!m.getConditionCode())
				nextPc = memAddr;
			
		} else if (op == 18) {  // BSA
			m.setMemoryAt(memAddr, pc + 1);
			nextPc = memAddr + 1;
			
		} else if (op == 19) {  // BIN
			nextPc = m.getMemoryAt(memAddr);
			
		} else if (op == 20) {  // INP
			try {
				int c = m.input();
				if (c != -1)
					m.setAccumulator(c);
				else
					nextPc = memAddr;
			} catch (IOException e) {
				throw new MachineIoException("Input stream exception", e);
			}
			
		} else if (op == 21) {  // OUT
			try {
				if (m.output(m.getAccumulator() & 0xFF))
					;  // Success, do nothing, go to next instruction
				else
					nextPc = memAddr;
			} catch (IOException e) {
				throw new MachineIoException("Output stream exception", e);
			}
			
		} else {  // Illegal
			m.setHalted(true);
			throw new IllegalOpcodeException(String.format("Illegal opcode 0x%02X at 0x%06X", op, pc));
		}
		
		// Set next program counter
		m.setProgramCounter(nextPc);
	}
	
	
	
	/**
	 * Not instantiable.
	 */
	private Executor() {}
	
}
