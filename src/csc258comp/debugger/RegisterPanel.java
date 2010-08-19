package csc258comp.debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


@SuppressWarnings("serial")
final class RegisterPanel extends JPanel {
	
	public JTextField stepCountField;
	public JTextField programCounter;
	public JTextField accumulator;
	public JTextField conditionCode;
	public JTextField nextInstruction;
	
	
	
	public RegisterPanel() {
		super(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.FIRST_LINE_START;
		g.insets = new Insets(4, 4, 4, 4);
		g.fill = GridBagConstraints.HORIZONTAL;
		
		g.gridx = 0;
		g.gridy = 0;
		add(new JLabel("Step count"), g);
		stepCountField = new JTextField("0");
		stepCountField.setEditable(false);
		stepCountField.setHorizontalAlignment(SwingConstants.RIGHT);
		stepCountField.setBackground(StatePanel.unchangedColor);
		stepCountField.setFont(StatePanel.monospacedFont);
		g.gridy = 1;
		add(stepCountField, g);
		
		g.gridx = 1;
		g.gridy = 0;
		add(new JLabel("Program counter"), g);
		programCounter = new JTextField();
		programCounter.setEditable(false);
		programCounter.setHorizontalAlignment(SwingConstants.RIGHT);
		programCounter.setBackground(StatePanel.unchangedColor);
		programCounter.setFont(StatePanel.monospacedFont);
		g.gridy = 1;
		add(programCounter, g);
		
		g.gridx = 2;
		g.gridy = 0;
		add(new JLabel("Accumulator"), g);
		accumulator = new JTextField();
		accumulator.setEditable(false);
		accumulator.setHorizontalAlignment(SwingConstants.RIGHT);
		accumulator.setBackground(StatePanel.unchangedColor);
		accumulator.setFont(StatePanel.monospacedFont);
		g.gridy = 1;
		add(accumulator, g);
		
		g.gridx = 3;
		g.gridy = 0;
		add(new JLabel("Condition code"), g);
		conditionCode = new JTextField();
		conditionCode.setEditable(false);
		conditionCode.setHorizontalAlignment(SwingConstants.RIGHT);
		conditionCode.setBackground(StatePanel.unchangedColor);
		conditionCode.setFont(StatePanel.monospacedFont);
		g.gridy = 1;
		add(conditionCode, g);
		
		g.gridx = 4;
		g.gridy = 0;
		add(new JLabel("Next instruction"), g);
		nextInstruction = new JTextField();
		nextInstruction.setEditable(false);
		nextInstruction.setHorizontalAlignment(SwingConstants.LEADING);
		nextInstruction.setBackground(StatePanel.unchangedColor);
		nextInstruction.setFont(StatePanel.monospacedFont);
		g.gridy = 1;
		add(nextInstruction, g);
		
		g.gridx = 4;
		g.gridy = 0;
	}
	
	
}
