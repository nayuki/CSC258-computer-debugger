package csc258comp.debugger;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import csc258comp.runner.Executor;
import csc258comp.runner.InstructionSet;
import csc258comp.runner.Machine;
import csc258comp.runner.MachineException;
import csc258comp.runner.Program;


@SuppressWarnings("serial")
final class StatePanel extends JPanel implements MachineStateListener {
	
	private static final Color unchangedColor = Color.WHITE;
	private static final Color changedColor = new Color(1.0f, 1.0f, 0.5f);
	
	private static final Font monospacedFont = new Font("Monospaced", Font.PLAIN, 12);
	
	
	private ProbedMachine machineState;
	
	private Set<Integer> breakpoints;
	
	private long stepCount;
	
	private JTextField stepCountField;
	private JTextField programCounter;
	private JTextField accumulator;
	private JTextField conditionCode;
	private JTextField nextInstruction;
	
	private MachineStateTableModel tableModel;
	

	private int oldProgramCounter;
	
	private boolean oldConditionCode;
	
	
	
	public StatePanel(final ProbedMachine machineState) {
		this.machineState = machineState;
		breakpoints = new HashSet<Integer>();
		stepCount = 0;
		
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.CENTER;
		g.insets = new Insets(4, 4, 4, 4);
		
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 0;
		g.weighty = 0;
		g.fill = GridBagConstraints.NONE;
		add(makeRegistersPanel(), g);
		
		g.gridx = 1;
		g.gridy = 0;
		g.weightx = 0;
		g.weighty = 0;
		g.fill = GridBagConstraints.NONE;
		add(makeActionsPanel(), g);
		
		g.gridx = 2;
		g.gridy = 0;
		g.weightx = 1;
		g.weighty = 0;
		g.fill = GridBagConstraints.HORIZONTAL;
		add(new JPanel(), g);
		
		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 3;
		g.weightx = 1;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		tableModel = new MachineStateTableModel(machineState, breakpoints);
		JTable table = new JTable(tableModel);
		table.setFont(monospacedFont);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int[] columnWidths = {30, 100, 80, 400};
		for (int i = 0; i < columnWidths.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
		table.setColumnSelectionAllowed(true);
		JScrollPane scrollpane = new JScrollPane(table);
		add(scrollpane, g);
		
		programCounterChanged(machineState);
		accumulatorChanged(machineState);
		conditionCodeChanged(machineState);
		
		machineState.addListener(this);
	}
	
	
	private JPanel makeRegistersPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.FIRST_LINE_START;
		g.insets = new Insets(4, 4, 4, 4);
		g.fill = GridBagConstraints.HORIZONTAL;
		
		g.gridx = 0;
		g.gridy = 0;
		panel.add(new JLabel("Step count"), g);
		stepCountField = new JTextField("0");
		stepCountField.setEditable(false);
		stepCountField.setHorizontalAlignment(SwingConstants.RIGHT);
		stepCountField.setBackground(unchangedColor);
		stepCountField.setFont(monospacedFont);
		g.gridy = 1;
		panel.add(stepCountField, g);
		
		g.gridx = 1;
		g.gridy = 0;
		panel.add(new JLabel("Program counter"), g);
		programCounter = new JTextField();
		programCounter.setEditable(false);
		programCounter.setHorizontalAlignment(SwingConstants.RIGHT);
		programCounter.setBackground(unchangedColor);
		programCounter.setFont(monospacedFont);
		g.gridy = 1;
		panel.add(programCounter, g);
		
		g.gridx = 2;
		g.gridy = 0;
		panel.add(new JLabel("Accumulator"), g);
		accumulator = new JTextField();
		accumulator.setEditable(false);
		accumulator.setHorizontalAlignment(SwingConstants.RIGHT);
		accumulator.setBackground(unchangedColor);
		accumulator.setFont(monospacedFont);
		g.gridy = 1;
		panel.add(accumulator, g);
		
		g.gridx = 3;
		g.gridy = 0;
		panel.add(new JLabel("Condition code"), g);
		conditionCode = new JTextField();
		conditionCode.setEditable(false);
		conditionCode.setHorizontalAlignment(SwingConstants.RIGHT);
		conditionCode.setBackground(unchangedColor);
		conditionCode.setFont(monospacedFont);
		g.gridy = 1;
		panel.add(conditionCode, g);
		
		g.gridx = 4;
		g.gridy = 0;
		panel.add(new JLabel("Next instruction"), g);
		nextInstruction = new JTextField();
		nextInstruction.setEditable(false);
		nextInstruction.setHorizontalAlignment(SwingConstants.LEADING);
		nextInstruction.setBackground(unchangedColor);
		nextInstruction.setFont(monospacedFont);
		g.gridy = 1;
		panel.add(nextInstruction, g);
		
		g.gridx = 4;
		g.gridy = 0;
		
		return panel;
	}
	
	
	private JComponent makeActionsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton step = new JButton("Step");
		step.addActionListener(new StepAction());
		panel.add(step);
		
		JButton run = new JButton("Run");
		run.addActionListener(new RunAction());
		panel.add(run);
		
		return panel;
	}
	
	
	@Override
	public void haltedChanged(Machine m) {}
	
	
	@Override
	public void programCounterChanged(Machine m) {
		int newProgramCounter = m.getProgramCounter();
		programCounter.setText(String.format("%06X", newProgramCounter));
		if (newProgramCounter != ((oldProgramCounter + 1) & 0xFFFFFF))
			programCounter.setBackground(changedColor);
		
		int nextinst = m.getMemoryAt(newProgramCounter);
		int opcode = nextinst >>> 24;
		String mnemonic = InstructionSet.getOpcodeName(opcode);
		if (mnemonic != null)
			nextInstruction.setText(String.format("%s %06X", mnemonic, nextinst & 0xFFFFFF));
		else {
			nextInstruction.setText(String.format("Invalid (%02X)", opcode));
			nextInstruction.setBackground(changedColor);
		}
		
		tableModel.fireTableRowsUpdated(oldProgramCounter, oldProgramCounter);
		tableModel.fireTableRowsUpdated(newProgramCounter, newProgramCounter);
	}
	
	
	@Override
	public void accumulatorChanged(Machine m) {
		accumulator.setText(String.format("%08X", m.getAccumulator() & 0xFFFFFFFFL));
		accumulator.setBackground(changedColor);
	}
	
	
	@Override
	public void conditionCodeChanged(Machine m) {
		conditionCode.setText(m.getConditionCode() ? "1" : "0");
		if (m.getConditionCode() != oldConditionCode)
			conditionCode.setBackground(changedColor);
	}
	
	
	@Override
	public void memoryChanged(Machine m, int addr) {
		tableModel.fireTableCellUpdated(addr, 2);
	}
	
	@Override
	public void programLoaded(Machine m, Program p) {
		programCounterChanged(m);
		programCounter.setBackground(unchangedColor);
		accumulator.setBackground(unchangedColor);
		conditionCode.setBackground(unchangedColor);
		nextInstruction.setBackground(unchangedColor);
		tableModel.program = p;
		tableModel.setRowCount(p.getImageSize());
	}
	
	
	
	private class StepAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if (machineState.isHalted())
				return;
			try {
				programCounter.setBackground(unchangedColor);
				accumulator.setBackground(unchangedColor);
				conditionCode.setBackground(unchangedColor);
				nextInstruction.setBackground(unchangedColor);
				oldConditionCode = machineState.getConditionCode();
				oldProgramCounter = machineState.getProgramCounter();
				if (!machineState.isHalted()) {
					Executor.step(machineState);
					stepCount++;
					stepCountField.setText(Long.toString(stepCount));
				}
			} catch (MachineException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
	
	
	private class RunAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			try {
				machineState.removeListener(StatePanel.this);
				while (!machineState.isHalted()) {
					Executor.step(machineState);
					stepCount++;
					stepCountField.setText(Long.toString(stepCount));
					if (breakpoints.contains(machineState.getProgramCounter()))
						break;
				}
				programCounterChanged(machineState);
				accumulatorChanged(machineState);
				conditionCodeChanged(machineState);
				machineState.addListener(StatePanel.this);
			} catch (MachineException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
}
