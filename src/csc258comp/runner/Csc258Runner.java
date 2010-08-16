package csc258comp.runner;
import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import csc258comp.compiler.CompilationException;
import csc258comp.compiler.Csc258Compiler;
import csc258comp.compiler.SourceCode;


public class Csc258Runner {
	
	public static void main(String[] args) throws IOException {
		SourceCode sc = SourceCode.readFile(new File(args[0]));
		
		Program p;
		try {
			p = Csc258Compiler.compile(sc);
		} catch (CompilationException e) {
			SortedMap<Integer,String> errorMessages = e.getErrorMessages();
			SourceCode sourceCode = e.getSourceCode();
			for (int line : errorMessages.keySet()) {
				System.err.printf("Line %d: %s%n", line + 1, errorMessages.get(line));
				System.err.println(sourceCode.getLineAt(line));
				System.err.println();
			}
			System.err.printf("%d error%s%n", errorMessages.size(), errorMessages.size() == 1 ? "" : "s");
			return;
		}
		
		Machine m = new SimpleMachine(System.in, System.out);
		Loader.load(m, p);
		
		try {
			while (!m.isHalted()) {
				Executor.step(m);
			}
		} catch (MachineException e) {
			System.err.println(e.getMessage());
			Throwable cause = e.getCause();
			if (cause != null)
				cause.printStackTrace();
			System.exit(1);
		}
	}
	
}
