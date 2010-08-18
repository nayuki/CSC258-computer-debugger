package csc258comp.compiler;

import java.util.HashMap;
import java.util.Map;

import csc258comp.runner.Executor;
import csc258comp.runner.Program;
import csc258comp.util.IntBuffer;


public final class Csc258Linker {
	
	public static Program link(Iterable<Fragment> frags) {
		if (frags == null)
			throw new NullPointerException();
		
		Map<String,Integer> alllabels = new HashMap<String,Integer>();
		alllabels.put("opsys", Executor.OPSYS_ADDRESS);
		
		// Union all labels and relocate them
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
		
		// Resolve references and build program image
		IntBuffer allimage = new IntBuffer();
		for (Fragment f : frags) {
			int[] image = resolveReferences(f, alllabels);
			allimage.append(image);
		}
		
		Map<SourceLine,Integer> srcLineToAddr = new HashMap<SourceLine,Integer>();
		Map<Integer,SourceLine> addrToSrcLine = new HashMap<Integer,SourceLine>();
		
		offset = 0;
		for (Fragment f : frags) {
			SourceCode src = f.getSourceCode();
			
			Map<Integer,Integer> aBySl = f.getAddressBySourceLineMap();
			for (int l : aBySl.keySet()) {
				srcLineToAddr.put(new SourceLine(src, l), aBySl.get(l) + offset);
			}
			
			Map<Integer,Integer> slByA = f.getSourceLineByAddressMap();
			for (int a : slByA.keySet()) {
				addrToSrcLine.put(a + offset, new SourceLine(src, slByA.get(a)));
			}
			
			offset += f.getImageLength();
		}
		
		return new Program(allimage.toArray(), alllabels.get("main"), srcLineToAddr, addrToSrcLine);
	}
	
	
	private static int[] resolveReferences(Fragment f, Map<String,Integer> alllabels) {
		int[] image = f.getImage();
		Map<Integer,String> refs = f.getReferences();
		for (int addr : refs.keySet()) {
			String label = refs.get(addr);
			if (!alllabels.containsKey(label))
				throw new IllegalArgumentException(String.format("Label \"%s\" not defined", label));
			image[addr] |= alllabels.get(label);  // Bottom 24 bits of instruction/data word are all zeros
		}
		return image;
	}
	
	
	
	private Csc258Linker() {}
	
}
