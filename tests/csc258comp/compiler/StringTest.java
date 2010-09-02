package csc258comp.compiler;

import org.junit.Test;


public final class StringTest {
	
	@Test
	public void testBlank() {
		DataTestUtils.test("d: C ''", 0x00000000);
	}
	
	
	@Test
	public void testOne() {
		DataTestUtils.test("d: C 'a'", 0x00000061);
	}
	
	
	// Catches naive tokenizers that split the source code by white spaces
	@Test
	public void testSpace() {
		DataTestUtils.test("d: C ' '", 0x00000020);
	}
	
	
	@Test
	public void testEscapes() {
		DataTestUtils.test("d: C '\\''", 0x00000027);
		DataTestUtils.test("d: C '\\\\'", 0x0000005C);
		DataTestUtils.test("d: C '\\0'", 0x00000000);
		DataTestUtils.test("d: C '\\b'", 0x00000008);
		DataTestUtils.test("d: C '\\n'", 0x0000000A);
		DataTestUtils.test("d: C '\\r'", 0x0000000D);
		DataTestUtils.test("d: C '\\t'", 0x00000009);
	}
	
	
	@Test
	public void testTwo() {
		DataTestUtils.test("d: C '01'", 0x00003130);
	}
	
	
	@Test
	public void testThree() {
		DataTestUtils.test("d: C 'bar'", 0x00726162);
	}
	
	
	@Test
	public void testFour() {
		DataTestUtils.test("d: C 'FOUR'", 0x52554F46);
	}
	
	
	@Test(expected=CompilerException.class)
	public void testNoString() throws CompilerException {
		DataTestUtils.testInvalid("C\nC 'Test'");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testUnclosed0() throws CompilerException {
		DataTestUtils.testInvalid("C '");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testUnclosed1() throws CompilerException {
		DataTestUtils.testInvalid("C '0");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testInvalidEscape0() throws CompilerException {
		DataTestUtils.testInvalid("C '\\a'");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testInvalidEscape1() throws CompilerException {
		DataTestUtils.testInvalid("C '\\f'");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testInvalidEscape2() throws CompilerException {
		DataTestUtils.testInvalid("C '\\v'");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testTooLong() throws CompilerException {
		DataTestUtils.testInvalid("C 'FIVER'");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testLongWithEscape() throws CompilerException {
		DataTestUtils.testInvalid("C '\\0\\b\\n\\r\\t'");
	}
	
}
