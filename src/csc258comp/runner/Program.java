package csc258comp.runner;

import java.util.Map;

import csc258comp.compiler.SourceLine;


public final class Program {
	
	private int[] image;
	
	private int mainAddress;
	
	private Map<SourceLine,Integer> sourceLineToAddress;
	private Map<Integer,SourceLine> addressToSourceLine;
	
	
	
	public Program(int[] image, int mainAddress, Map<SourceLine,Integer> srcLineToAddr, Map<Integer,SourceLine> addrToSrcLine) {
		if (image == null)
			throw new NullPointerException();
		if (image.length > Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Invalid image, exceeds size of address space");
		if (mainAddress < 0 || mainAddress >= Machine.ADDRESS_SPACE_SIZE)
			throw new IllegalArgumentException("Invalid address for main");
		
		this.image = image.clone();
		this.mainAddress = mainAddress;
		sourceLineToAddress = srcLineToAddr;
		addressToSourceLine = addrToSrcLine;
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
	
	
	public String getSourceLine(int addr) {
		SourceLine sl = addressToSourceLine.get(addr);
		if (sl != null)
			return sl.getString();
		else
			return null;
	}
	
	
	public String toString() {
		return String.format("%s (%d words)", super.toString(), image.length);
	}
	
}
