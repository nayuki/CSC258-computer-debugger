package csc258comp.debugger;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import javax.swing.*;

import csc258comp.compiler.*;


public class Csc258Debugger {
	
	public static void main(String[] args) throws IOException {
		SourceCode s = SourceCode.readFile(new File(args[0]));
		Program p;
		try {
			p = Program.parseProgram(s);
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
		
		ProbedMachineState m = new ProbedMachineState();
		StatePanel panel = new StatePanel(m);
		m.loadProgram(p);
		
		JFrame frame = new JFrame("CSC258 Computer Debugger");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
}
