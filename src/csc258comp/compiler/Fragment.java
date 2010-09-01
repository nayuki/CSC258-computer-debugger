package csc258comp.compiler;

import java.util.Collections;
import java.util.Map;

import csc258comp.runner.Machine;
import csc258comp.runner.Program;


/**
 * Represents a machine code image, along with labels declared and references used. Line-address mapping information for debugging is optionally included. Immutable.
 * @see Program
 * @see MyCompiler
 * @see Linker
 */
public final class Fragment {
	
	private final int[] image;
	
	private final Map<String,Integer> labels;
	private final Map<Integer,String> references;
	
	private final SourceCode sourceCode;
	private final Map<Integer,Integer> sourceLineToAddress;
	private final Map<Integer,Integer> addressToSourceLine;
	
	
	
	public Fragment(int[] image, Map<String,Integer> labels, Map<Integer,String> references, SourceCode source, Map<Integer,Integer> srcLineToAddr, Map<Integer,Integer> addrToSrcLine) {
		if (image == null || labels == null || references == null)
			throw new NullPointerException();
		if (image.length > Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Invalid image, exceeds size of address space");
		
		this.image = image.clone();
		this.labels = Collections.unmodifiableMap(labels);
		this.references = Collections.unmodifiableMap(references);
		
		if (source != null) {
			if (srcLineToAddr == null || addrToSrcLine == null)
				throw new NullPointerException();
			sourceCode = source;
			sourceLineToAddress = Collections.unmodifiableMap(srcLineToAddr);
			addressToSourceLine = Collections.unmodifiableMap(addrToSrcLine);
		} else {
			if (srcLineToAddr != null || addrToSrcLine != null)
				throw new IllegalArgumentException();
			sourceCode = null;
			sourceLineToAddress = null;
			addressToSourceLine = null;
		}
	}
	
	
	
	public int[] getImage() {
		return image.clone();
	}
	
	
	public int getImageLength() {
		return image.length;
	}
	
	
	public Map<String,Integer> getLabels() {
		return labels;
	}
	
	
	public Map<Integer,String> getReferences() {
		return references;
	}
	
	
	public SourceCode getSourceCode() {
		return sourceCode;
	}
	
	
	public Map<Integer,Integer> getSourceLineToAddressMap() {
		return sourceLineToAddress;
	}
	
	
	public Map<Integer,Integer> getAddressToSourceLineMap() {
		return addressToSourceLine;
	}
	
}
