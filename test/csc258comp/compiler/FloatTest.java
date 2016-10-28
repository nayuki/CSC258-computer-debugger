package csc258comp.compiler;

import org.junit.Test;


public final class FloatTest {
	
	@Test
	public void testBasic() {
		DataTestUtils.test("d: F 0", 0x00000000);
		DataTestUtils.test("d: F 1.0", 0x3F800000);
		DataTestUtils.test("d: F 0.25", 0x3E800000);
		DataTestUtils.test("d: F -3", 0xC0400000);
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNoNumber() throws CompilerException {
		DataTestUtils.testInvalid("F\nF 0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testInvalid() throws CompilerException {
		DataTestUtils.testInvalid("F abcd");
	}
	
}
