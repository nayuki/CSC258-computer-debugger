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
	
	
	
	public RegisterPanel(DebugPanel parent) {
		super(new GridBagLayout());
		if (parent == null)
			throw new NullPointerException();
		
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.FIRST_LINE_START;
		g.insets = new Insets(4, 4, 4, 4);
		g.fill = GridBagConstraints.HORIZONTAL;
		
		g.gridx = 0;
		g.gridy = 0;
		add(new JLabel("Step count"), g);
		stepCountField = newJTextField(SwingConstants.RIGHT);
		g.gridy = 1;
		add(stepCountField, g);
		
		g.gridx = 1;
		g.gridy = 0;
		add(new JLabel("Program counter"), g);
		programCounter = newJTextField(SwingConstants.RIGHT);
		g.gridy = 1;
		add(programCounter, g);
		
		g.gridx = 2;
		g.gridy = 0;
		add(new JLabel("Accumulator"), g);
		accumulator = newJTextField(SwingConstants.RIGHT);
		g.gridy = 1;
		add(accumulator, g);
		
		g.gridx = 3;
		g.gridy = 0;
		add(new JLabel("Condition code"), g);
		conditionCode = newJTextField(SwingConstants.RIGHT);
		g.gridy = 1;
		add(conditionCode, g);
		
		g.gridx = 4;
		g.gridy = 0;
		add(new JLabel("Next instruction"), g);
		nextInstruction = newJTextField(SwingConstants.LEADING);
		g.gridy = 1;
		add(nextInstruction, g);
		
		g.gridx = 4;
		g.gridy = 0;
		
		DebugMachine m = parent.machine;
		m.addListener(this);
		programCounterChanged(m);
		accumulatorChanged(m);
		conditionCodeChanged(m);
	}
	
	
	private static JTextField newJTextField(int horizAlign) {
		JTextField result = new JTextField();
		result.setEditable(false);
		result.setHorizontalAlignment(horizAlign);
		result.setBackground(DebugPanel.unchangedColor);
		result.setFont(DebugPanel.monospacedFont);
		return result;
	}
	
	
	
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
