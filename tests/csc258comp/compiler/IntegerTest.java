package csc258comp.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


public class IntegerTest {
	
	@Test
	public void testBasic() {
		test("n: I 0", 0);
		test("n: I 1", 1);
		test("n: I 10", 10);
		test("n: I -1", -1);
	}
	
	
	@Test
	public void testUpperLimit() {
		test("n: I 2147483639", 2147483639);
		test("n: I 2147483646", 2147483646);
		test("n: I 2147483647", 2147483647);
	}
	
	
	@Test
	public void testLowerLimit() {
		test("n: I -2147483639", -2147483639);
		test("n: I -2147483647", -2147483647);
		test("n: I -2147483648", -2147483648);
	}
	
	
	@Test(expected=CompilationException.class)
	public void testInvalid() throws CompilationException {
		testInvalid("I abcd");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testPlusSign() throws CompilationException {
		testInvalid("I +0");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testPositiveOverflow() throws CompilationException {
		testInvalid("I 2147483648");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testNegativeOverflow() throws CompilationException {
		testInvalid("I -2147483649");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testBigPositiveOverflow() throws CompilationException {
		testInvalid("I 99999999999999999999999999999999999999999999999999");
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
