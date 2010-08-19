package csc258comp.compiler;

import org.junit.Test;


public class BinaryTest {
	
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
	
	
	@Test(expected=CompilationException.class)
	public void testInvalid() throws CompilationException {
		DataTestUtils.testInvalid("B 201");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testPlusSign() throws CompilationException {
		DataTestUtils.testInvalid("B +0");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testNegativeZero() throws CompilationException {
		DataTestUtils.testInvalid("B -0");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testNegative() throws CompilationException {
		DataTestUtils.testInvalid("B -1");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testOverflow() throws CompilationException {
		DataTestUtils.testInvalid("B 100000000000000000000000000000000");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testBigOverflow() throws CompilationException {
		DataTestUtils.testInvalid("B 11111111111111111111111111111111111111111111111111111111111111111111111111111111");
	}
	
}
