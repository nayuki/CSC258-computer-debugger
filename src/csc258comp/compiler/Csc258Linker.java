package csc258comp.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import csc258comp.runner.Program;
import csc258comp.util.IntBuffer;


public final class Csc258Linker {
	
	public static Program link(Set<Fragment> frags) {
		// Union all labels and relocate them
		Map<String,Integer> alllabels = new HashMap<String,Integer>();
		int offset = 0;
		for (Fragment f : frags) {
			Map<String,Integer> labels = f.getLabels();
			for (String label : labels.keySet()) {
				if (alllabels.containsKey(label))
					throw new IllegalArgumentException(String.format("Duplicate label \"%s\"", label));
				else
					alllabels.put(label, labels.get(label) + offset);
			}
			offset += f.getImageLength();
		}
		
		IntBuffer allimage = new IntBuffer();
		// Resolve references and build program image
		for (Fragment f : frags) {
			int[] image = f.getImage();
			Map<Integer,String> refs = f.getReferences();
			for (int addr : refs.keySet()) {
				String label = refs.get(addr);
				if (!alllabels.containsKey(label))
					throw new IllegalArgumentException(String.format("Label \"%s\" not defined", label));
				image[addr] |= alllabels.get(label);  // Bottom 24 bits of instruction/data word are all zeros
			}
			allimage.append(image);
		}
		
		return new Program(allimage.toArray(), alllabels.get("main"), new HashMap<Integer,String>());
	}
	
	
	
	private Csc258Linker() {}
	
}
