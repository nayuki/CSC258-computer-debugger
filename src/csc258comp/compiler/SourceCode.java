package csc258comp.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public final class SourceCode implements Iterable<String> {
	
	public static SourceCode readFile(File file) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		try {
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				lines.add(line);
			}
		} finally {
			in.close();
		}
		return new SourceCode(lines);
	}
	
	
	
	private List<String> lines;
	
	
	public SourceCode(List<String> lines) {
		this.lines = Collections.unmodifiableList(new ArrayList<String>(lines));
	}
	
	
	
	public int getLineCount() {
		return lines.size();
	}
	
	
	public String getLineAt(int row) {
		return lines.get(row);
	}
	
	
	public Iterator<String> iterator() {
		return lines.iterator();
	}
	
}
