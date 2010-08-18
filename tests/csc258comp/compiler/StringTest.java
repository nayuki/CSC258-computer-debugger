package csc258comp.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


public class StringTest {
	
	@Test
	public void testBlank() {
		test("s: C ''", 0x00000000);
	}
	
	
	@Test
	public void testOne() {
		test("s: C 'a'", 0x00000061);
	}
	
	
	@Test
	public void testEscapes() {
		test("s: C '\\''", 0x00000027);
		test("s: C '\\\\'", 0x0000005C);
		test("s: C '\\0'", 0x00000000);
		test("s: C '\\b'", 0x00000008);
		test("s: C '\\n'", 0x0000000A);
		test("s: C '\\r'", 0x0000000D);
		test("s: C '\\t'", 0x00000009);
	}
	
	
	@Test
	public void testTwo() {
		test("s: C '01'", 0x00003031);
	}
	
	
	@Test
	public void testThree() {
		test("s: C 'bar'", 0x00626172);
	}
	
	
	@Test
	public void testFour() {
		test("s: C 'FOUR'", 0x464F5552);
	}
	
	
	@Test(expected=CompilationException.class)
	public void testInvalidEscape0() throws CompilationException {
		testInvalid("C '\\a'");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testInvalidEscape1() throws CompilationException {
		testInvalid("C '\\f'");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testInvalidEscape2() throws CompilationException {
		testInvalid("C '\\v'");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testTooLong() throws CompilationException {
		testInvalid("C 'FIVER'");
	}
	
	
	@Test(expected=CompilationException.class)
	public void testLongWithEscape() throws CompilationException {
		testInvalid("C '\\0\\b\\n\\r\\t'");
	}
	
	
	
	private static void test(String code, int expectedWord) {
		try {
			List<String> lines = new ArrayList<String>();
			Collections.addAll(lines, code.split("\n"));
			
			SourceCode source = new SourceCode(lines);
			Fragment f = Csc258Compiler.compile(source);
			
			int addr = f.getLabels().get("s");
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
