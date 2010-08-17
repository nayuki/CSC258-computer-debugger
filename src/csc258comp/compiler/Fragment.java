package csc258comp.compiler;

import java.util.Collections;
import java.util.Map;


public final class Fragment {
	
	private int[] image;
	
	private Map<String,Integer> labels;
	private Map<Integer,String> references;
	
	private SourceCode sourceCode;
	private Map<Integer,Integer> addressBySourceLine;
	private Map<Integer,Integer> sourceLineByAddress;
	
	
	
	public Fragment(int[] image, Map<String,Integer> labels, Map<Integer,String> references, SourceCode source, Map<Integer,Integer> addrBySrcLine, Map<Integer,Integer> srcLineByAddr) {
		this.image = image.clone();
		this.labels = Collections.unmodifiableMap(labels);
		this.references = Collections.unmodifiableMap(references);
		
		if (source != null) {
			if (addrBySrcLine == null || srcLineByAddr == null)
				throw new NullPointerException();
			sourceCode = source;
			addressBySourceLine = Collections.unmodifiableMap(addrBySrcLine);
			sourceLineByAddress = Collections.unmodifiableMap(srcLineByAddr);
		} else {
			if (addrBySrcLine != null || srcLineByAddr != null)
				throw new IllegalArgumentException();
			sourceCode = null;
			addressBySourceLine = null;
			sourceLineByAddress = null;
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
		return addressBySourceLine;
	}
	
	
	public Map<Integer,Integer> getSourceLineByAddressMap() {
		return sourceLineByAddress;
	}
	
}
