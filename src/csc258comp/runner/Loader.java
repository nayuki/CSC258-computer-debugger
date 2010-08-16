package csc258comp.runner;


public final class Loader {
	
	public static void load(Machine m, Program p) {
		m.setHalted(false);
		m.setProgramCounter(p.getMainAddress());
		m.setAccumulator(0);
		m.setConditionCode(false);
		
		int[] image = p.getImage();
		for (int j = 0; j < image.length; j++)
			m.setMemoryAt(j, image[j]);
	}
	
}
