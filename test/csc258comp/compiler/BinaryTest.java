package csc258comp.compiler;

import org.junit.Test;


public final class BinaryTest {
	
	@Test
	public void testBasic() {
		DataTestUtils.test("d: B 0", 0);
		DataTestUtils.test("d: B 1", 1);
		DataTestUtils.test("d: B 1101", 13);
	}
	
	
	@Test
	public void testZeroPadding() {
		DataTestUtils.test("d: B 00000000000000000000000000000000000000001", 1);
	}
	
	
	@Test
	public void testUpperLimit() {
		DataTestUtils.test("d: B 11111111111111111111111111111111", 0xFFFFFFFF);
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNoNumber() throws CompilerException {
		DataTestUtils.testInvalid("B\nB 0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testInvalid() throws CompilerException {
		DataTestUtils.testInvalid("B 201");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testPlusSign() throws CompilerException {
		DataTestUtils.testInvalid("B +0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNegativeZero() throws CompilerException {
		DataTestUtils.testInvalid("B -0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNegative() throws CompilerException {
		DataTestUtils.testInvalid("B -1");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testOverflow() throws CompilerException {
		DataTestUtils.testInvalid("B 100000000000000000000000000000000");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testBigOverflow() throws CompilerException {
		DataTestUtils.testInvalid("B 11111111111111111111111111111111111111111111111111111111111111111111111111111111");
	}
	
}
