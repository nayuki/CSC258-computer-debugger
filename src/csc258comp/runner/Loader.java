package csc258comp.runner;


/**
 * Loads a {@link Program} into a {@link Machine}.
 */
public final class Loader {
	
	public static void load(Machine m, Program p) {
		if (m == null || p == null)
			throw new NullPointerException();
		m.setHalted(false);
		m.setProgramCounter(p.getMainAddress());
		m.setAccumulator(0);
		m.setConditionCode(false);
		
		int[] image = p.getImage();
		for (int j = 0; j < image.length; j++)
			m.setMemoryAt(j, image[j]);
	}
	
	
	
	/**
	 * Not instantiable.
	 */
	private Loader() {}
	
}
