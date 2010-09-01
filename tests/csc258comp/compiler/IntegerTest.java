package csc258comp.compiler;

import org.junit.Test;


public final class IntegerTest {
	
	@Test
	public void testBasic() {
		DataTestUtils.test("d: I 0", 0);
		DataTestUtils.test("d: I 1", 1);
		DataTestUtils.test("d: I 10", 10);
		DataTestUtils.test("d: I -1", -1);
	}
	
	
	@Test
	public void testZeroPadding() {
		DataTestUtils.test("d: I 000000000000000000001", 1);
	}
	
	
	@Test
	public void testUpperLimit() {
		DataTestUtils.test("d: I 2147483639", 2147483639);
		DataTestUtils.test("d: I 2147483646", 2147483646);
		DataTestUtils.test("d: I 2147483647", 2147483647);
	}
	
	
	@Test
	public void testLowerLimit() {
		DataTestUtils.test("d: I -2147483639", -2147483639);
		DataTestUtils.test("d: I -2147483647", -2147483647);
		DataTestUtils.test("d: I -2147483648", -2147483648);
	}
	
	
	@Test(expected=CompilerException.class)
	public void testInvalid() throws CompilerException {
		DataTestUtils.testInvalid("I abcd");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testPlusSign() throws CompilerException {
		DataTestUtils.testInvalid("I +0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testPositiveOverflow() throws CompilerException {
		DataTestUtils.testInvalid("I 2147483648");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNegativeOverflow() throws CompilerException {
		DataTestUtils.testInvalid("I -2147483649");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testBigPositiveOverflow() throws CompilerException {
		DataTestUtils.testInvalid("I 99999999999999999999999999999999999999999999999999");
	}
	
}
