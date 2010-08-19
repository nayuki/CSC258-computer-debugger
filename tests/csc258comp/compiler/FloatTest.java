package csc258comp.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


public class FloatTest {
	
	@Test
	public void testBasic() {
		test("n: F 0", 0x00000000);
		test("n: F 1.0", 0x3F800000);
		test("n: F 0.25", 0x3E800000);
		test("n: F -3", 0xC0400000);
	}
	
	
	@Test(expected=CompilationException.class)
	public void testInvalid() throws CompilationException {
		testInvalid("F abcd");
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
