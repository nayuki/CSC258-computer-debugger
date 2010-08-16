package csc258comp.machine.model;

import java.io.IOException;


/**
 * A CSC258 computer machine.
 */
public interface Machine {
	
	/**
	 * Tests whether this machine is halted.
	 * @return whether this machine is halted
	 */
	public boolean isHalted();
	
	/**
	 * Sets whether this machine is halted.
	 * @param halted whether this machine is halted
	 */
	public void setHalted(boolean halted);
	
	
	/**
	 * Returns the value of this machine's program counter.
	 * @return the value of this machine's program counter
	 * @throws IllegalArgumentException if the address is not in the range [0, 2<sup>24</sup>)
	 */
	public int getProgramCounter();
	
	/**
	 * Sets the value of this machine's program counter to the specified address.
	 * @param addr the program counter
	 */
	public void setProgramCounter(int addr);
	
	
	/**
	 * Returns the value of this machine's accumulator register.
	 * @return the value of this machine's accumulator register
	 */
	public int getAccumulator();
	
	/**
	 * Sets the value of this machine's accumulator register to the specified address.
	 */
	public void setAccumulator(int val);
	
	
	/**
	 * Tests whether this machine's condition code is set.
	 * @return whether this machine's condition code is set
	 */
	public boolean getConditionCode();
	
	/**
	 * Sets the Boolean value of this machine's condition code.
	 * @param value the value of this machine's condition code
	 */
	public void setConditionCode(boolean val);
	
	
	/**
	 * Returns the contents of this machine's memory at the specified address.
	 * @param addr the memory address, in the range [0, 2<sup>24</sup>)
	 * @return the contents of this machine's memory at the address
	 * @throws IllegalArgumentException if the address is not in the range [0, 2<sup>24</sup>)
	 */
	public int getMemoryAt(int addr);
	
	/**
	 * Sets the contents of this machine's memory at the specified address to the specified 32-bit value.
	 * @param addr the memory address, in the range [0, 2<sup>24</sup>)
	 * @throws IllegalArgumentException if the address is not in the range [0, 2<sup>24</sup>)
	 */
	public void setMemoryAt(int addr, int val);
	
	
	/**
	 * Returns the next byte of input data from this machine's input stream, or -1 if the input is not ready.
	 * @return the next byte of input, or -1 if the input is not ready
	 * @throws IOException if an I/O exception occurred
	 */
	public int input() throws IOException;
	
	/**
	 * Tries to write the specified byte of data to this machine's output stream, and returns whether the write succeeded. The byte value must be in the range [0, 256).
	 * @param b the byte to write
	 * @return whether the write succeeded
	 * @throws IllegalArgumentException if the byte value is not in the range [0, 256)
	 * @throws IOException if an I/O exception occurred
	 */
	public boolean output(int b) throws IOException;
	
}
