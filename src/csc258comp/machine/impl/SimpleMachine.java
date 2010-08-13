package csc258comp.machine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import csc258comp.machine.model.Machine;
import csc258comp.machine.model.MachineState;


public final class SimpleMachine implements Machine {
	
	private MachineState state;
	
	private InputStream input;
	
	private OutputStream output;
	
	
	
	public SimpleMachine(InputStream in, OutputStream out) {
		state = new SimpleMachineState();
		input = in;
		output = out;
	}
	
	
	
	@Override
	public int input() throws IOException {
		if (input.available() == 0)
			return -1;
		else
			return input.read();
	}
	
	
	@Override
	public boolean output(int b) throws IOException {
		output.write(b);
		return true;
	}
	
	
	@Override
	public MachineState getState() {
		return state;
	}
	
}
