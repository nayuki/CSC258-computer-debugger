import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

import csc258comp.compiler.CompilationException;
import csc258comp.compiler.Csc258Compiler;
import csc258comp.compiler.Program;
import csc258comp.compiler.SourceCode;
import csc258comp.machine.impl.Executor;
import csc258comp.machine.impl.SimpleMachine;
import csc258comp.machine.model.Machine;
import csc258comp.machine.model.MachineState;


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
		
		MachineState st = m.getState();
		st.setHalted(false);
		st.setProgramCounter(p.getMainAddress());
		st.setAccumulator(0);
		st.setConditionCode(false);
		
		int[] image = p.getImage();
		for (int j = 0; j < image.length; j++)
			st.setMemoryAt(j, image[j]);
		
		while (!st.isHalted()) {
			Executor.step(m);
		}
	}
	
}
