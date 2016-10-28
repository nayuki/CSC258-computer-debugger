/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;


public class RememberingInputStreamTest {
	
	@Test
	public void testRead() throws IOException {
		byte[] data = {2, 3, 1, 4};
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		RememberingInputStream rin = new RememberingInputStream(bin);
		assertEquals(data[0], rin.read());
		assertEquals(data[1], rin.read());
		assertEquals(data[2], rin.read());
		assertEquals(data[3], rin.read());
	}
	
	
	@Test
	public void testCloneRead() throws IOException {
		byte[] data = {8, 0, 6};
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		RememberingInputStream rin0 = new RememberingInputStream(bin);
		RememberingInputStream rin1 = rin0.clone();
		assertEquals(data[0], rin0.read());
		assertEquals(data[1], rin0.read());
		assertEquals(data[2], rin0.read());
		assertEquals(data[0], rin1.read());
		assertEquals(data[1], rin1.read());
		assertEquals(data[2], rin1.read());
	}
	
	
	@Test
	public void testReadCloneRead() throws IOException {
		byte[] data = {7, 0, 2, 5};
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		RememberingInputStream rin0 = new RememberingInputStream(bin);
		assertEquals(data[0], rin0.read());
		assertEquals(data[1], rin0.read());
		RememberingInputStream rin1 = rin0.clone();
		assertEquals(data[2], rin0.read());
		assertEquals(data[3], rin0.read());
		assertEquals(data[2], rin1.read());
		assertEquals(data[3], rin1.read());
	}
	
}
