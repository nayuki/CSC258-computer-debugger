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
	
	
	@Test(expected=CompilationException.class)
	public void testInvalid() throws CompilationException {
		DataTestUtils.testInvalid("H foobar");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testPlusSign() throws CompilationException {
		DataTestUtils.testInvalid("H +0");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testNegativeZero() throws CompilationException {
		DataTestUtils.testInvalid("H -0");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testNegative() throws CompilationException {
		DataTestUtils.testInvalid("H -1");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testOverflow() throws CompilationException {
		DataTestUtils.testInvalid("H 100000000");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testBigOverflow() throws CompilationException {
		DataTestUtils.testInvalid("H FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
	}
	
}
