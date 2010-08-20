package csc258comp.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import csc258comp.compiler.CompilationException;
import csc258comp.compiler.Csc258Compiler;
import csc258comp.compiler.Csc258Linker;
import csc258comp.compiler.Fragment;
import csc258comp.compiler.SourceCode;


public final class Csc258Runner {
	
	public static void main(String[] args) throws IOException {
		List<Fragment> frags = new ArrayList<Fragment>();
		for (String arg : args) {
			try {
				File file = new File(arg);
				SourceCode sc = SourceCode.readFile(file);
				Fragment f;
				try {
					f = Csc258Compiler.compile(sc);
				} catch (OutOfMemoryError e) {
					System.err.println("Error: Out of memory during compilation");
					System.exit(1);
					return;
				}
				frags.add(f);
			} catch (CompilationException e) {
				printCompilerErrors(e.getErrorMessages(), e.getSourceCode());
				System.exit(1);
				return;
			}
		}
		
		Program p;
		try {
			p = Csc258Linker.link(frags);
		} catch (OutOfMemoryError e) {
			System.err.println("Error: Out of memory during linking");
			System.exit(1);
			return;
		}
		Machine m = new BasicMachine(System.in, System.out);
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
	
	
	private static void printCompilerErrors(SortedMap<Integer,String> msgs, SourceCode sc) {
		String filename = sc.getFile() != null ? sc.getFile().getName() : "(no file)";
		for (int line : msgs.keySet()) {
			System.err.printf("%s:%d: %s%n", filename, line + 1, msgs.get(line));
			System.err.println(sc.getLineAt(line));
			System.err.println();
		}
		System.err.printf("%d error%s%n", msgs.size(), msgs.size() == 1 ? "" : "s");
	}
	
}
