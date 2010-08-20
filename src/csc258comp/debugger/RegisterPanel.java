package csc258comp.debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import csc258comp.runner.InstructionSet;
import csc258comp.runner.Machine;
import csc258comp.runner.Program;


@SuppressWarnings("serial")
final class RegisterPanel extends JPanel implements MachineListener {
	
	public final JTextField stepCountField;
	public final JTextField programCounter;
	public final JTextField accumulator;
	public final JTextField conditionCode;
	public final JTextField nextInstruction;
	
	private final DebugMachine machine;
	private final Controller controller;
	
	private int oldProgramCounter;
	private int oldAccumulator;
	private boolean oldConditionCode;
	
	
	
	public RegisterPanel(DebugPanel parent) {
		super(new GridBagLayout());
		if (parent == null)
			throw new NullPointerException();
		controller = parent.controller;
		machine = parent.machine;
		
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.FIRST_LINE_START;
		g.insets = new Insets(4, 4, 4, 4);
		g.fill = GridBagConstraints.HORIZONTAL;
		
		g.gridx = 0;
		g.gridy = 0;
		add(new JLabel("Step count"), g);
		g.gridy = 1;
		stepCountField = newJTextField(SwingConstants.RIGHT);
		add(stepCountField, g);
		
		g.gridx = 1;
		g.gridy = 0;
		add(new JLabel("Program counter"), g);
		g.gridy = 1;
		programCounter = newJTextField(SwingConstants.RIGHT);
		add(programCounter, g);
		
		g.gridx = 2;
		g.gridy = 0;
		add(new JLabel("Accumulator"), g);
		g.gridy = 1;
		accumulator = newJTextField(SwingConstants.RIGHT);
		add(accumulator, g);
		
		g.gridx = 3;
		g.gridy = 0;
		add(new JLabel("Condition code"), g);
		g.gridy = 1;
		conditionCode = newJTextField(SwingConstants.RIGHT);
		add(conditionCode, g);
		
		g.gridx = 4;
		g.gridy = 0;
		add(new JLabel("Next instruction"), g);
		g.gridy = 1;
		nextInstruction = newJTextField(SwingConstants.LEADING);
		add(nextInstruction, g);
		
		machine.addListener(this);
		programCounterChanged(machine);
		accumulatorChanged(machine);
		conditionCodeChanged(machine);
	}
	
	
	private static JTextField newJTextField(int horizAlign) {
		JTextField result = new JTextField();
		result.setEditable(false);
		result.setHorizontalAlignment(horizAlign);
		result.setBackground(DebugPanel.unchangedColor);
		result.setFont(DebugPanel.monospacedFont);
		return result;
	}
	
	
	
	// Execution handlers
	
	public void beginStep() {
		oldProgramCounter = machine.getProgramCounter();
		oldAccumulator = machine.getAccumulator();
		oldConditionCode = machine.getConditionCode();
	}
	
	
	public void beginRun() {
		oldAccumulator = machine.getAccumulator();
		oldConditionCode = machine.getConditionCode();
		machine.removeListener(this);
	}
	
	
	public void endStep() {
		programCounterChanged(machine);
		accumulatorChanged(machine);
		conditionCodeChanged(machine);
		stepCountField.setText(Long.toString(controller.getStepCount()));
		
		programCounter.setBackground(machine.getProgramCounter() == ((oldProgramCounter + 1) & 0xFFFFFF) ? DebugPanel.unchangedColor : DebugPanel.changedColor);
		accumulator.setBackground(machine.getAccumulator() == oldAccumulator ? DebugPanel.unchangedColor : DebugPanel.changedColor);
		conditionCode.setBackground(machine.getConditionCode() == oldConditionCode ? DebugPanel.unchangedColor : DebugPanel.changedColor);
	}
	
	
	public void endRun() {
		programCounterChanged(machine);
		accumulatorChanged(machine);
		conditionCodeChanged(machine);
		stepCountField.setText(Long.toString(controller.getStepCount()));
		
		programCounter.setBackground(DebugPanel.unchangedColor);
		accumulator.setBackground(machine.getAccumulator() == oldAccumulator ? DebugPanel.unchangedColor : DebugPanel.changedColor);
		conditionCode.setBackground(machine.getConditionCode() == oldConditionCode ? DebugPanel.unchangedColor : DebugPanel.changedColor);
		
		machine.addListener(this);
	}
	
	
	
	// Machine state change handlers
	
	@Override
	public void programCounterChanged(Machine m) {
		int newProgramCounter = m.getProgramCounter();
		programCounter.setText(String.format("%06X", newProgramCounter));
		
		int nextinst = m.getMemoryAt(newProgramCounter);
		int opcode = nextinst >>> 24;
		String mnemonic = InstructionSet.getOpcodeName(opcode);
		if (mnemonic != null)
			nextInstruction.setText(String.format("%s %06X", mnemonic, nextinst & 0xFFFFFF));
		else {
			nextInstruction.setText(String.format("Invalid (%02X)", opcode));
			nextInstruction.setBackground(DebugPanel.changedColor);
		}
	}
	
	
	@Override
	public void accumulatorChanged(Machine m) {
		accumulator.setText(String.format("%08X", m.getAccumulator() & 0xFFFFFFFFL));
		accumulator.setBackground(DebugPanel.changedColor);
	}
	
	
	@Override
	public void conditionCodeChanged(Machine m) {
		conditionCode.setText(m.getConditionCode() ? "1" : "0");
	}
	
	
	@Override
	public void programLoaded(Machine m, Program p) {
		programCounter.setBackground(DebugPanel.unchangedColor);
		accumulator.setBackground(DebugPanel.unchangedColor);
		conditionCode.setBackground(DebugPanel.unchangedColor);
		nextInstruction.setBackground(DebugPanel.unchangedColor);
	}
	
	
	@Override
	public void haltedChanged(Machine m) {}
	
	@Override
	public void memoryChanged(Machine m, int addr) {}
	
}
