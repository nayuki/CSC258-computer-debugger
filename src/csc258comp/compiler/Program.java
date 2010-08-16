package csc258comp.compiler;

import java.util.Map;


public class Program {
	
	public static final int OPSYS_ADDRESS = 0x31337;
	
	
	
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
