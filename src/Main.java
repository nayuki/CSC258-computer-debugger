import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import csc258comp.*;


public class Main {
	
	public static void main(String[] args) throws IOException, CompilationException {
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
		MachineState m = new SimpleMachineState();
		m.setHalted(false);
		m.setProgramCounter(p.getMainAddress());
		m.setAccumulator(0);
		m.setConditionCode(false);
		int[] image = p.getImage();
		for (int j = 0; j < image.length; j++)
			m.setMemoryAt(j, image[j]);
		Executor e = new Executor();
		while (!m.getHalted()) {
			e.step(m);
		}
	}
	
}