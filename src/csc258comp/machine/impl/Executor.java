package csc258comp.machine.impl;

import java.io.IOException;

import csc258comp.compiler.Program;
import csc258comp.machine.model.Machine;
import csc258comp.machine.model.MachineState;


public final class Executor {
	
	public static void step(Machine m) throws IOException {
		MachineState s = m.getState();
		
		if (s.isHalted())
			return;
		
		int instAddr = s.getProgramCounter();
		if (instAddr == Program.OPSYS_ADDRESS) {
			s.setHalted(true);
			return;
		}
		
		int word = s.getMemoryAt(instAddr);
		int oper = word >>> 24;
		int addr = word & ((1 << 24) - 1);
		int nextInstAddr;
		
		if (oper == 0) {  // LDA
			s.setAccumulator(s.getMemoryAt(addr));
			nextInstAddr = instAddr + 1;
				
		} else if (oper == 1) {  // STA
			s.setMemoryAt(addr, s.getAccumulator());
			nextInstAddr = instAddr + 1;
				
		} else if (oper >= 2 && oper <= 6 || oper >= 13 && oper <= 15) {  // ADD, SUB, MUL, DIV, MOD, AND, IOR, XOR
			int x = s.getAccumulator();
			int y = s.getMemoryAt(addr);
			int result;
			switch (oper) {
				case  2:  result = x + y;  break;
				case  3:  result = x - y;  break;
				case  4:  result = x * y;  break;
				case  5:  result = (y != 0 ? x / y : x);  break;
				case  6:  result = (y != 0 ? x % y : x);  break;
				case 13:  result = x & y;  break;
				case 14:  result = x | y;  break;
				case 15:  result = x ^ y;  break;
				default:  throw new AssertionError();
			}
			s.setAccumulator(result);
			if (oper == 2)
				s.setConditionCode(result != (long)x + y);
			else if (oper == 3)
				s.setConditionCode(result != (long)x - y);
			else if (oper == 4)
				s.setConditionCode(result != (long)x * y);
			else if (oper == 5 || oper == 6)
				s.setConditionCode(y == 0);
			else
				s.setConditionCode(result != 0);
			nextInstAddr = instAddr + 1;
				
		} else if (oper >= 7 && oper <= 10) {  // FLA, FLS, FLM, FLD
			float x = Float.intBitsToFloat(s.getAccumulator());
			float y = Float.intBitsToFloat(s.getMemoryAt(addr));
			float result;
			switch (oper) {
				case  7:  result = x + y;  break;
				case  8:  result = x - y;  break;
				case  9:  result = x * y;  break;
				case 10:  result = (y != 0 ? x / y : x);  break;
				default:  throw new AssertionError();
			}
			s.setAccumulator(Float.floatToRawIntBits(result));
			s.setConditionCode(Float.isInfinite(result));
			if (oper >= 7 && oper <= 9)
				s.setConditionCode(false);  // Overflow detection not implemented
			else if (y == 0)
				s.setConditionCode(true);
			nextInstAddr = instAddr + 1;
			
		} else if (oper == 11) {  // CIF
			s.setAccumulator(Float.floatToRawIntBits((float)s.getMemoryAt(addr)));
			nextInstAddr = instAddr + 1;
			
		} else if (oper == 12) {  // CFI
			s.setAccumulator((int)Math.round(Float.intBitsToFloat(s.getMemoryAt(addr))));
			nextInstAddr = instAddr + 1;
			
		} else if (oper >= 16 && oper <= 19) {  // BUN, BZE, BSA, BIN
			switch (oper) {
				case 16:  nextInstAddr = addr;  break;
				case 17:  nextInstAddr = (s.getConditionCode() ? instAddr + 1 : addr);  break;
				case 18:  s.setMemoryAt(addr, instAddr + 1);  nextInstAddr = addr + 1;  break;
				case 19:  nextInstAddr = s.getMemoryAt(addr);  break;
				default:  throw new AssertionError();
			}
			
		} else if (oper == 20) {  // INP
			int c = m.input();
			if (c != -1) {
				s.setAccumulator(c);
				nextInstAddr = instAddr + 1;
			} else {  // EOF reached
				nextInstAddr = addr;
			}
			
		} else if (oper == 21) {  // OUT
			if (m.output(s.getAccumulator() & 0xFF)) {
				nextInstAddr = instAddr + 1;
			} else {
				nextInstAddr = addr;
			}
			
		} else {
			throw new IllegalStateException("Illegal opcode");
		}
		
		s.setProgramCounter(nextInstAddr);
	}
	
	
	private Executor() {}
	
}
