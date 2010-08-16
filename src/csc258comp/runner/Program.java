package csc258comp.runner;

import java.util.Map;


public class Program {
	
	private int[] image;
	
	private int mainAddress;
	
	private Map<Integer,String> imageSourceCode;
	
	
	
	public Program(int[] image, int mainAddress, Map<Integer,String> imageSourceCode) {
		this.image = image.clone();
		this.mainAddress = mainAddress;
		this.imageSourceCode = imageSourceCode;
	}
	
	
	
	public int getImageSize() {
		return image.length;
	}
	
	
	public int[] getImage() {
		return image.clone();
	}
	
	
	public int getMainAddress() {
		return mainAddress;
	}
	
	
	public String getSourceLine(int index) {
		return imageSourceCode.get(index);
	}
	
}
