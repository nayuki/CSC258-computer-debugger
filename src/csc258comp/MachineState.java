package csc258comp;


public interface MachineState {
	
	public boolean isHalted();
	
	public void setHalted(boolean halted);
	
	
	public int getProgramCounter();
	
	public void setProgramCounter(int addr);
	
	
	public int getAccumulator();
	
	public void setAccumulator(int value);
	
	
	public boolean getConditionCode();
	
	public void setConditionCode(boolean value);
	
	
	public int getMemoryAt(int addr);
	
	public void setMemoryAt(int addr, int value);
	
}