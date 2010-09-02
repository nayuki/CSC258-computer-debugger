package csc258comp.compiler;

import java.util.HashMap;
import java.util.Map;

import csc258comp.runner.Executor;
import csc258comp.runner.Loader;
import csc258comp.runner.Machine;
import csc258comp.runner.Program;


/**
 * Links some {@link Fragment} objects to create a {@link Program} object. This class contains just a static method, and is not instantiable.
 * @see Compiler
 * @see Loader
 */
public final class Linker {
	
	/**
	 * Returns a program created from linking the specified fragments.
	 * @param frags the fragments to link
	 * @return a program from linking the fragments
	 * @throws NullPointerException if {@code frags} is {@code null} or if any of its elements are {@code null}
	 * @throws LinkerException if the linkage cannot be completed
	 */
	public static Program link(Iterable<Fragment> frags) {
		if (frags == null)
			throw new NullPointerException();
		return new Linker(frags).result;
	}
	
	
	
	private int imageSize;  // Set only once, by layOutFragments()
	
	private final Program result;
	
	private final Map<SourceLine,String> errorMessages = new HashMap<SourceLine,String>();
	
	
	
	private Linker(Iterable<Fragment> frags) {
		// Lay out the fragments in the address space
		Map<Fragment,Integer> fragmentToOffset = layOutFragments(frags);  // Also, imageSize is set
		
		// Gather all the label definitions
		Map<String,Integer> allLabels = unionLabels(fragmentToOffset, frags);
		
		// Build the program image
		int[] image = resolveAndBuildImage(frags, fragmentToOffset, allLabels, imageSize);
		
		// Generate debugging info
		Map<SourceLine,Integer> srcLineToAddr = new HashMap<SourceLine,Integer>();
		Map<Integer,SourceLine> addrToSrcLine = new HashMap<Integer,SourceLine>();
		for (Fragment f : frags) {
			SourceCode src = f.getSourceCode();
			int off = fragmentToOffset.get(f);
			
			Map<Integer,Integer> aBySl = f.getSourceLineToAddressMap();
			for (int l : aBySl.keySet()) {
				srcLineToAddr.put(new SourceLine(src, l), aBySl.get(l) + off);
			}
			
			Map<Integer,Integer> slByA = f.getAddressToSourceLineMap();
			for (int a : slByA.keySet()) {
				addrToSrcLine.put(a + off, new SourceLine(src, slByA.get(a)));
			}
		}
		
		// If there are error messages, then throw an exception and don't return a program
		if (errorMessages.size() > 0)
			throw new LinkerException(String.format("%d linker errors", errorMessages.size()), errorMessages);
		
		// Check for label "main"
		if (!allLabels.containsKey("main"))
			throw new LinkerException("Label \"main\" not defined");
		
		// Construct the program
		result = new Program(image, allLabels.get("main"), srcLineToAddr, addrToSrcLine);
	}
	
	
	// Almost pure function - pure except that imageSize is set
	private Map<Fragment,Integer> layOutFragments(Iterable<Fragment> frags) {
		Map<Fragment,Integer> fragToOff = new HashMap<Fragment,Integer>();
		int offset = 0;
		for (Fragment f : frags) {
			fragToOff.put(f, offset);
			offset += f.getImageLength();
			if (offset > Machine.ADDRESS_SPACE_SIZE)
				throw new LinkerException("Images too large for address space");
		}
		imageSize = offset;
		return fragToOff;
	}
	
	
	// Pure function
	private Map<String,Integer> unionLabels(Map<Fragment,Integer> fragToOff, Iterable<Fragment> frags) {
		Map<String,Integer> alllabels = new HashMap<String,Integer>();
		alllabels.put("opsys", Executor.OPSYS_ADDRESS);
		
		for (Fragment f : frags) {
			int off = fragToOff.get(f);
			Map<String,Integer> labels = f.getLabels();
			for (String label : labels.keySet()) {
				if (!alllabels.containsKey(label))
					alllabels.put(label, labels.get(label) + off);  // Relocation happens here
				else {
					SourceLine sl = new SourceLine(f.getSourceCode(), f.getAddressToSourceLineMap().get(labels.get(label)));
					errorMessages.put(sl, String.format("Duplicate label \"%s\"", label));
				}
			}
		}
		
		return alllabels;
	}
	
	
	// Pure function
	private int[] resolveAndBuildImage(Iterable<Fragment> frags, Map<Fragment,Integer> fragToOff, Map<String,Integer> allLabels, int imageSize) {
		int[] allImage = new int[imageSize];
		for (Fragment f : frags) {
			int[] image = getImageAndResolveReferences(f, allLabels);
			System.arraycopy(image, 0, allImage, fragToOff.get(f), image.length);
		}
		return allImage;
	}
	
	
	// Pure function
	private int[] getImageAndResolveReferences(Fragment f, Map<String,Integer> alllabels) {
		int[] image = f.getImage();
		Map<Integer,String> refs = f.getReferences();
		for (int addr : refs.keySet()) {
			String label = refs.get(addr);
			if (alllabels.containsKey(label))
				image[addr] |= alllabels.get(label);  // Bottom 24 bits of instruction/data word are all zeros
			else {
				SourceLine sl = new SourceLine(f.getSourceCode(), f.getAddressToSourceLineMap().get(addr));
				errorMessages.put(sl, String.format("Label \"%s\" not defined", label));
			}
		}
		return image;
	}
	
}
