package csc258comp.compiler;

import org.junit.Test;


public final class CompilerTest {
	
	@Test(expected=CompilerException.class)
	public void testUnexpectedChar0() {
		DataTestUtils.compile("\u00FFADD foo");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testUnexpectedChar1() {
		DataTestUtils.compile("SUB\u0080 foo");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testUnexpectedChar2() {
		DataTestUtils.compile("MUL \u2000 foo");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testUnexpectedChar3() {
		DataTestUtils.compile("DIV \u0300foo");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testLabelWithNoWord() {
		DataTestUtils.compile("bar:");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testUndefinedMnemonic() {
		DataTestUtils.compile("XXX foo");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testMissingReference() {
		DataTestUtils.compile("LDA\nSTA foo");
	}
	
	
	@Test(expected=CompilerException.class)
	public void testDuplicateLabel() {
		DataTestUtils.compile("foo: I 0\nfoo: I 1");
	}
	
}
