package csc258comp.compiler;

import java.util.Collections;
import java.util.Map;


public final class Fragment {
	
	private int[] image;
	
	private Map<String,Integer> labels;
	private Map<Integer,String> references;
	
	private SourceCode sourceCode;
	private Map<Integer,Integer> sourceLineToAddress;
	private Map<Integer,Integer> addressToSourceLine;
	
	
	
	public Fragment(int[] image, Map<String,Integer> labels, Map<Integer,String> references, SourceCode source, Map<Integer,Integer> srcLineToAddr, Map<Integer,Integer> addrToSrcLine) {
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
	
	
	public Map<Integer,Integer> getAddressBySourceLineMap() {
		return sourceLineToAddress;
	}
	
	
	public Map<Integer,Integer> getSourceLineByAddressMap() {
		return addressToSourceLine;
	}
	
}
