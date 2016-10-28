/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.compiler;

import org.junit.Test;


public final class HexadecimalTest {
	
	@Test
	public void testBasic() {
		DataTestUtils.test("d: H 0", 0);
		DataTestUtils.test("d: H 1", 1);
		DataTestUtils.test("d: H FF", 0xFF);
		DataTestUtils.test("d: H cafe", 0xCAFE);
	}
	
	
	@Test
	public void testZeroPadding() {
		DataTestUtils.test("d: H 000000000000000000001", 1);
	}
	
	
	@Test
	public void testUpperLimit() {
		DataTestUtils.test("d: H FFFFFFFF", 0xFFFFFFFF);
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNoNumber() throws CompilerException {
		DataTestUtils.testInvalid("H\nH 0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testInvalid() throws CompilerException {
		DataTestUtils.testInvalid("H foobar");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testPlusSign() throws CompilerException {
		DataTestUtils.testInvalid("H +0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNegativeZero() throws CompilerException {
		DataTestUtils.testInvalid("H -0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNegative() throws CompilerException {
		DataTestUtils.testInvalid("H -1");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testOverflow() throws CompilerException {
		DataTestUtils.testInvalid("H 100000000");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testBigOverflow() throws CompilerException {
		DataTestUtils.testInvalid("H FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
	}
	
}
