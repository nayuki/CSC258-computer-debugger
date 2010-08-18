package csc258comp.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


public class BinaryTest {
	
	@Test
	public void testBasic() {
		test("n: B 0", 0);
		test("n: B 1", 1);
		test("n: B 1101", 13);
	}
	
	
	@Test
	public void testZeroPadding() {
		test("n: B 00000000000000000000000000000000000000001", 1);
	}
	
	
	@Test
	public void testUpperLimit() {
		test("n: B 11111111111111111111111111111111", 0xFFFFFFFF);
	}
	
	
	@Test(expected=CompilationException.class)
	public void testInvalid() throws CompilationException {
		testInvalid("B 201");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testPlusSign() throws CompilationException {
		testInvalid("B +0");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testNegativeZero() throws CompilationException {
		testInvalid("B -0");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testNegative() throws CompilationException {
		testInvalid("B -1");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testOverflow() throws CompilationException {
		testInvalid("B 100000000000000000000000000000000");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testBigOverflow() throws CompilationException {
		testInvalid("B 11111111111111111111111111111111111111111111111111111111111111111111111111111111");
	}
	
	
	
	private static void test(String code, int expectedWord) {
		try {
			List<String> lines = new ArrayList<String>();
			Collections.addAll(lines, code.split("\n"));
			
			SourceCode source = new SourceCode(lines);
			Fragment f = Csc258Compiler.compile(source);
			
			int addr = f.getLabels().get("n");
			int word = f.getImage()[addr];
			assertEquals(expectedWord, word);
		} catch (CompilationException e) {
			fail("Compiler exception: " + e.getMessage());
		}
	}
	
	
	private static void testInvalid(String code) throws CompilationException {
		List<String> lines = new ArrayList<String>();
		Collections.addAll(lines, code.split("\n"));
		
		SourceCode source = new SourceCode(lines);
		Csc258Compiler.compile(source);
	}
	
}
