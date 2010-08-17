package csc258comp.runner;

import java.util.Map;

import csc258comp.compiler.SourceLine;


public final class Program {
	
	private int[] image;
	
	private int mainAddress;
	
	private Map<SourceLine,Integer> addressBySourceLine;
	private Map<Integer,SourceLine> sourceLineByAddress;
	
	
	
	public Program(int[] image, int mainAddress, Map<SourceLine,Integer> addrBySrcLine, Map<Integer,SourceLine> srcLineByAddr) {
		if (image == null)
			throw new NullPointerException();
		this.image = image.clone();
		this.mainAddress = mainAddress;
		addressBySourceLine = addrBySrcLine;
		sourceLineByAddress = srcLineByAddr;
		
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
		return sourceLineByAddress.get(addr).getString();
	}
	
}
