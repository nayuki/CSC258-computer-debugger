package csc258comp.runner;


/**
 * Loads a {@link Program} into a {@link Machine}. This class contains just a static method, and is not instantiable.
 */
public final class Loader {
	
	/**
	 * Loads the specified program onto the specified machine. Haltedness is set to {@code false}, the program counter is set to the address of {@code main}, the accumulator is set to 0, and the condition code is set to {@false}. The portion of the memory covered by the program image is set to the contents of the program image. The rest of the memory is unchanged. 
	 * @param m the machine to load the program onto
	 * @param p the program to load
	 * @throws NullPointerException if {@code m} or {@code p} are {@code null}
	 */
	public static void load(Machine m, Program p) {
		if (m == null || p == null)
			throw new NullPointerException();
		
		// Set registers and haltedness
		m.setHalted(false);
		m.setProgramCounter(p.getMainAddress());
		m.setAccumulator(0);
		m.setConditionCode(false);
		
		// Set memory
		int[] image = p.getImage();
		for (int j = 0; j < image.length; j++)
			m.setMemoryAt(j, image[j]);
	}
	
	
	
	/**
	 * Not instantiable.
	 */
	private Loader() {}
	
}
