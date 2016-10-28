/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


final class DataTestUtils {
	
	public static Fragment compile(String code) {
		SourceCode source = new SourceCode(code);
		return MyCompiler.compile(source);
	}
	
	
	public static void test(String code, int expectedWord) {
		try {
			Fragment f = compile(code);
			
			int addr = f.getLabels().get("d");
			int word = f.getImage()[addr];
			assertEquals(expectedWord, word);
		} catch (CompilerException e) {
			fail("Compiler exception: " + e.getMessage());
		}
	}
	
	
	public static void testInvalid(String code) throws CompilerException {
		SourceCode source = new SourceCode(code);
		MyCompiler.compile(source);
	}
	
}
