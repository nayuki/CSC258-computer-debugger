import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import csc258comp.CompilationException;
import csc258comp.Executor;
import csc258comp.MachineState;
import csc258comp.Program;
import csc258comp.SimpleMachineState;
import csc258comp.SourceCode;


public class TicTacToeTester {
	
	public static void main(String[] args) throws IOException, CompilationException {
		SourceCode s = SourceCode.readFile(new File(args[0]));
		Program p = Program.parseProgram(s);
		int[] image = p.getImage();
		MachineState m = new SimpleMachineState();
		
		for (int i = 0; i < 19683; i++) {
			byte[] board = numberToBoard(i);
			ByteArrayInputStream in = new ByteArrayInputStream(board);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			m.setHalted(false);
			m.setProgramCounter(p.getMainAddress());
			m.setAccumulator(0);
			m.setConditionCode(false);
			for (int j = 0; j < image.length; j++)
				m.setMemoryAt(j, image[j]);
			Executor e = new Executor(in, out);
			while (!m.getHalted()) {
				e.step(m);
			}
			
			boolean xWins = checkWin(board, (byte)'X');
			boolean oWins = checkWin(board, (byte)'O');
			String expectedOutput;
			if (xWins && oWins) expectedOutput = "Error\n";
			else if (!xWins && !oWins) expectedOutput = "Neither X nor O wins the game.\n";
			else if (xWins) expectedOutput = "X wins the game.\n";
			else if (oWins) expectedOutput = "O wins the game.\n";
			else throw new AssertionError();
			if (!Arrays.equals(out.toByteArray(), expectedOutput.getBytes("US-ASCII"))) {
				System.out.printf("Fail on board <%s>%n", new String(board, "US-ASCII"));
				return;
			}
		}
		System.out.println("Pass");
	}


	private static byte[] numberToBoard(int boardnum) {
		if (boardnum < 0 || boardnum >= 19683)
			throw new IllegalArgumentException();
		byte[] board = new byte[9];
		for (int i = 0; i < 9; i++) {
			switch (boardnum % 3) {
				case 0: board[i] = ' '; break;
				case 1: board[i] = 'X'; break;
				case 2: board[i] = 'O'; break;
				default: throw new AssertionError();
			}
			boardnum /= 3;
		}
		return board;
	}
	
	
	private static boolean checkWin(byte[] board, byte player) {
		// Check rows
		for (int y = 0; y < 3; y++) {
			boolean win = true;
			for (int x = 0; x < 3; x++)
				win &= board[y * 3 + x] == player;
			if (win)
				return true;
		}
		
		// Check columns
		for (int x = 0; x < 3; x++) {
			boolean win = true;
			for (int y = 0; y < 3; y++)
				win &= board[y * 3 + x] == player;
			if (win)
				return true;
		}
		
		// Check forward diagonal
		{
			boolean win = true;
			for (int x = 0; x < 3; x++)
				win &= board[x * 3 + x] == player;
			if (win)
				return true;
		}
		
		// Check reverse diagonal
		{
			boolean win = true;
			for (int x = 0; x < 3; x++)
				win &= board[x * 3 + (3 - 1 - x)] == player;
			if (win)
				return true;
		}
		
		return false;
	}
	
}