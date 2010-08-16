package csc258comp.machine.impl;

import java.io.IOException;

import csc258comp.compiler.Program;
import csc258comp.machine.model.Machine;


public final class Executor {
	
	/**
	 * Executes one instruction on the specified machine.
	 * @param m the machine to execute on
	 * @throws IllegalStateException if an invalid opcode is encountered
	 * @throws IOException if an I/O exception occurs
	 */
	public static void step(Machine s) throws IOException {
		// Do nothing if halted
		if (s.isHalted())
			return;
		
		// Check if halting
		int pc = s.getProgramCounter();
		if (pc == Program.OPSYS_ADDRESS) {
			s.setHalted(true);
			return;
		}
		
		// Fetch instruction
		int instWord = s.getMemoryAt(pc);
		int op = instWord >>> 24;
		int memAddr = instWord & ((1 << 24) - 1);  // The memory address embedded in the instruction word
		int nextPc = -1;  // -1 is the default value that signifies that nextPc should be pc + 1
		
		// Execute instruction
		if (op == 0) {  // LDA
			s.setAccumulator(s.getMemoryAt(memAddr));
			
		} else if (op == 1) {  // STA
			s.setMemoryAt(memAddr, s.getAccumulator());
			
		} else if (op >= 2 && op <= 6 || op >= 13 && op <= 15) {  // ADD, SUB, MUL, DIV, MOD, AND, IOR, XOR
			int x = s.getAccumulator();
			int y = s.getMemoryAt(memAddr);
			int result;
			switch (op) {
				case  2:  result = x + y;  s.setConditionCode(result != (long)x + y);  break;
				case  3:  result = x - y;  s.setConditionCode(result != (long)x - y);  break;
				case  4:  result = x * y;  s.setConditionCode(result != (long)x * y);  break;
				case  5:  result = (y != 0 ? x / y : x);  s.setConditionCode(y == 0);  break;
				case  6:  result = (y != 0 ? x % y : x);  s.setConditionCode(y == 0);  break;
				case 13:  result = x & y;  s.setConditionCode(result != 0);  break;
				case 14:  result = x | y;  s.setConditionCode(result != 0);  break;
				case 15:  result = x ^ y;  s.setConditionCode(result != 0);  break;
				default:  throw new AssertionError();
			}
			s.setAccumulator(result);
			
		} else if (op >= 7 && op <= 10) {  // FLA, FLS, FLM, FLD
			float x = Float.intBitsToFloat(s.getAccumulator());
			float y = Float.intBitsToFloat(s.getMemoryAt(memAddr));
			float result;
			switch (op) {
				case  7:  result = x + y;  break;
				case  8:  result = x - y;  break;
				case  9:  result = x * y;  break;
				case 10:  result = (y != 0 ? x / y : x);  break;
				default:  throw new AssertionError();
			}
			s.setAccumulator(Float.floatToRawIntBits(result));
			s.setConditionCode(Float.isInfinite(result) || op == 10 && y == 0);
			
		} else if (op == 11) {  // CIF
			int ival = s.getMemoryAt(memAddr);
			float fval = ival;  // Implicit cast to float32
			int bits = Float.floatToRawIntBits(fval);
			s.setAccumulator(bits);
			
		} else if (op == 12) {  // CFI
			int bits = s.getMemoryAt(memAddr);
			float fval = Float.intBitsToFloat(bits);
			int ival = Math.round(fval);
			s.setAccumulator(ival);
			
		} else if (op >= 16 && op <= 19) {  // BUN, BZE, BSA, BIN
			switch (op) {
				case 16:  nextPc = memAddr;  break;
				case 17:  nextPc = (s.getConditionCode() ? pc + 1 : memAddr);  break;
				case 18:  s.setMemoryAt(memAddr, pc + 1);  nextPc = memAddr + 1;  break;
				case 19:  nextPc = s.getMemoryAt(memAddr);  break;
				default:  throw new AssertionError();
			}
			
		} else if (op == 20) {  // INP
			int c = s.input();
			if (c != -1)
				s.setAccumulator(c);
			else
				nextPc = memAddr;
			
		} else if (op == 21) {  // OUT
			if (s.output(s.getAccumulator() & 0xFF))
				;  // Success, do nothing, go to next instruction
			else
				nextPc = memAddr;
			
		} else {  // Illegal
			throw new IllegalStateException("Illegal opcode");
		}
		
		// Set next program counter
		if (nextPc == -1)
			nextPc = pc + 1;
		s.setProgramCounter(nextPc);
	}
	
	
	
	private Executor() {}
	
}
