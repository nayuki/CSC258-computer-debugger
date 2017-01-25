/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.runner;

import java.util.Map;
import java.util.Objects;
import csc258comp.compiler.Fragment;
import csc258comp.compiler.Linker;
import csc258comp.compiler.SourceLine;


/**
 * Represents a loadable, executable machine code image. The image contains no unresolved references. Line-address mapping information for debugging is optionally included. Immutable.
 * @see Fragment
 * @see Linker
 * @see Loader
 */
public final class Program {
	
	private final int[] image;
	
	private final int mainAddress;
	
	private final Map<SourceLine,Integer> sourceLineToAddress;
	private final Map<Integer,SourceLine> addressToSourceLine;
	
	
	
	public Program(int[] image, int mainAddress, Map<SourceLine,Integer> srcLineToAddr, Map<Integer,SourceLine> addrToSrcLine) {
		Objects.requireNonNull(image);
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
		if (addressToSourceLine.containsKey(addr))
			return addressToSourceLine.get(addr).getString();
		else
			return null;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s (%d words)", super.toString(), image.length);
	}
	
}
