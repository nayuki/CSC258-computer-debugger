package csc258comp.debugger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.JFrame;

import csc258comp.compiler.CompilationException;
import csc258comp.compiler.LinkerException;
import csc258comp.compiler.MyCompiler;
import csc258comp.compiler.Linker;
import csc258comp.compiler.Fragment;
import csc258comp.compiler.SourceCode;
import csc258comp.compiler.SourceLine;
import csc258comp.runner.Program;


public final class Debugger {
	
	public static void main(String[] args) throws IOException {
		List<Fragment> frags = new ArrayList<Fragment>();
		for (String arg : args) {
			try {
				File file = new File(arg);
				SourceCode sc = SourceCode.readFile(file);
				Fragment f;
				try {
					f = MyCompiler.compile(sc);
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
			p = Linker.link(frags);
		} catch (LinkerException e) {
			if (e.getErrorMessages() == null)
				System.err.printf("Linker error: %s%n", e.getMessage());
			else
				printLinkerErrors(e.getErrorMessages());
			System.exit(1);
			return;
		} catch (OutOfMemoryError e) {
			System.err.println("Error: Out of memory during linking");
			System.exit(1);
			return;
		}
		
		DebugMachine m = new DebugMachine(System.in, System.out);
		m.loadProgram(p);
		final DebugPanel panel = new DebugPanel(m, p);
		
		final JFrame frame = new JFrame("CSC258 Computer Debugger");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				panel.controller.suspend();
				frame.dispose();
			}
		});
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	private static void printCompilerErrors(SortedMap<Integer,String> msgs, SourceCode sc) {
		String filename = formatFileName(sc.getFile());
		for (int line : msgs.keySet()) {
			System.err.printf("%s:%d: %s%n", filename, line + 1, msgs.get(line));
			System.err.println(sc.getLineAt(line));
			System.err.println();
		}
		System.err.printf("%d error%s%n", msgs.size(), msgs.size() == 1 ? "" : "s");
	}
	
	
	private static void printLinkerErrors(Map<SourceLine,String> msgs) {
		for (SourceLine line : msgs.keySet()) {
			System.err.printf("%s:%d: %s%n", formatFileName(line.sourceCode.getFile()), line.lineNumber + 1, msgs.get(line));
			System.err.println(line.sourceCode.getLineAt(line.lineNumber));
			System.err.println();
		}
		System.err.printf("%d error%s%n", msgs.size(), msgs.size() == 1 ? "" : "s");
	}
	
	
	private static String formatFileName(File file) {
		if (file != null)
			return file.getName();
		else
			return "(no file)";
	}
	
}
