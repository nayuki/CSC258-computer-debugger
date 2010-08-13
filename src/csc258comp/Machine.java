package csc258comp;

import java.io.IOException;


/**
 * A CSC258 computer machine.
 */
public interface Machine {
	
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
	
	
	/**
	 * Returns the machine state. The state is a mutable object.
	 * @return the machine state
	 */
	public MachineState getState();
	
}