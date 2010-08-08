package csc258comp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Executor {
	
	private InputStream inputStream;
	
	private OutputStream outputStream;
	
	
	
	public Executor() {
		this(System.in, System.out);
	}
	
	
	public Executor(InputStream in, OutputStream out) {
		inputStream = in;
		outputStream = out;
	}
	
	
	
	public void step(MachineState m) throws IOException {
		if (m.getHalted())
			return;
		
		int instAddr = m.getProgramCounter();
		if (instAddr == Program.OPSYS_ADDRESS) {
			m.setHalted(true);
			return;
		}
		
		int word = m.getMemoryAt(instAddr);
		int oper = word >>> 24;
		int addr = word & ((1 << 24) - 1);
		int nextInstAddr;
		
		if (oper == 0) {  // LDA
			m.setAccumulator(m.getMemoryAt(addr));
			nextInstAddr = instAddr + 1;
				
		} else if (oper == 1) {  // STA
			m.setMemoryAt(addr, m.getAccumulator());
			nextInstAddr = instAddr + 1;
				
		} else if (oper >= 2 && oper <= 6 || oper >= 13 && oper <= 15) {  // ADD, SUB, MUL, DIV, MOD, AND, IOR, XOR
			int x = m.getAccumulator();
			int y = m.getMemoryAt(addr);
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
			m.setAccumulator(result);
			if (oper == 2)
				m.setConditionCode(result != (long)x + y);
			else if (oper == 3)
				m.setConditionCode(result != (long)x - y);
			else if (oper == 4)
				m.setConditionCode(result != (long)x * y);
			else if (oper == 5 || oper == 6)
				m.setConditionCode(y == 0);
			else
				m.setConditionCode(result != 0);
			nextInstAddr = instAddr + 1;
				
		} else if (oper >= 7 && oper <= 10) {  // FLA, FLS, FLM, FLD
			float x = Float.intBitsToFloat(m.getAccumulator());
			float y = Float.intBitsToFloat(m.getMemoryAt(addr));
			float result;
			switch (oper) {
				case  7:  result = x + y;  break;
				case  8:  result = x - y;  break;
				case  9:  result = x * y;  break;
				case 10:  result = (y != 0 ? x / y : x);  break;
				default:  throw new AssertionError();
			}
			m.setAccumulator(Float.floatToRawIntBits(result));
			m.setConditionCode(Float.isInfinite(result));
			if (oper >= 7 && oper <= 9)
				m.setConditionCode(false);  // Overflow detection not implemented
			else if (y == 0)
				m.setConditionCode(true);
			nextInstAddr = instAddr + 1;
			
		} else if (oper == 11) {  // CIF
			m.setAccumulator(Float.floatToRawIntBits((float)m.getMemoryAt(addr)));
			nextInstAddr = instAddr + 1;
			
		} else if (oper == 12) {  // CFI
			m.setAccumulator((int)Math.round(Float.intBitsToFloat(m.getMemoryAt(addr))));
			nextInstAddr = instAddr + 1;
			
		} else if (oper >= 16 && oper <= 19) {  // BUN, BZE, BSA, BIN
			switch (oper) {
				case 16:  nextInstAddr = addr;  break;
				case 17:  nextInstAddr = (m.getConditionCode() ? instAddr + 1 : addr);  break;
				case 18:  m.setMemoryAt(addr, instAddr + 1);  nextInstAddr = addr + 1;  break;
				case 19:  nextInstAddr = m.getMemoryAt(addr);  break;
				default:  throw new AssertionError();
			}
			
		} else if (oper == 20) {  // INP
			int c = inputStream.read();
			if (c != -1) {
				m.setAccumulator(c);
				nextInstAddr = instAddr + 1;
			} else {  // EOF reached
				nextInstAddr = addr;
			}
			
		} else if (oper == 21) {  // OUT
			outputStream.write(m.getAccumulator() & 0xFF);
			outputStream.flush();
			nextInstAddr = instAddr + 1;
			
		} else {
			throw new IllegalStateException("Illegal opcode");
		}
		
		m.setProgramCounter(nextInstAddr);
	}
	
}