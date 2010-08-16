package csc258comp.debugger;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import javax.swing.JFrame;

import csc258comp.compiler.CompilationException;
import csc258comp.compiler.Csc258Compiler;
import csc258comp.compiler.Program;
import csc258comp.compiler.SourceCode;


public class Csc258Debugger {
	
	public static void main(String[] args) throws IOException {
		SourceCode s = SourceCode.readFile(new File(args[0]));
		Program p;
		try {
			p = Csc258Compiler.compile(s);
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
		
		ProbedMachine m = new ProbedMachine(System.in, System.out);
		StatePanel panel = new StatePanel(m);
		m.loadProgram(p);
		
		JFrame frame = new JFrame("CSC258 Computer Debugger");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
}
