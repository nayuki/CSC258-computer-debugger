package csc258comp.compiler;

import java.util.Collections;
import java.util.Map;


public final class Fragment {
	
	private int[] image;
	
	private Map<String,Integer> labels;
	
	private Map<Integer,String> references;
	
	private Map<Integer,String> imageSourceCode;
	
	
	
	public Fragment(int[] image, Map<String,Integer> labels, Map<Integer,String> references, Map<Integer,String> imageSourceCode) {
		this.image = image.clone();
		this.labels = Collections.unmodifiableMap(labels);
		this.references = Collections.unmodifiableMap(references);
		this.imageSourceCode = imageSourceCode;
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
	
	
	public String getSourceLine(int index) {
		return imageSourceCode.get(index);
	}
	
}
