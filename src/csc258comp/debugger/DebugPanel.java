package csc258comp.debugger;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import csc258comp.runner.InstructionSet;
import csc258comp.runner.Machine;
import csc258comp.runner.Program;


@SuppressWarnings("serial")
final class DebugPanel extends JPanel implements MachineListener {
	
	static final Color unchangedColor = Color.WHITE;
	static final Color changedColor = new Color(1.0f, 1.0f, 0.5f);
	
	static final Font monospacedFont = new Font("Monospaced", Font.PLAIN, 12);
	
	
	DebugMachine machineState;
	
	Controller controller;
	
	RegisterPanel registerPanel;
	
	
	MachineTableModel tableModel;
	
	
	
	public DebugPanel(final DebugMachine machineState) {
		if (machineState == null)
			throw new NullPointerException();
		this.machineState = machineState;
		controller = new Controller(machineState);
		
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.CENTER;
		g.insets = new Insets(4, 4, 4, 4);
		
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 0;
		g.weighty = 0;
		g.fill = GridBagConstraints.NONE;
		registerPanel = new RegisterPanel();
		add(registerPanel, g);
		
		g.gridx = 1;
		g.gridy = 0;
		g.weightx = 0;
		g.weighty = 0;
		g.fill = GridBagConstraints.NONE;
		add(new ControlPanel(this), g);
		
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
		tableModel = new MachineTableModel(this);
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
	
	
	
	@Override
	public void haltedChanged(Machine m) {}
	
	
	@Override
	public void programCounterChanged(Machine m) {
		int newProgramCounter = m.getProgramCounter();
		registerPanel.programCounter.setText(String.format("%06X", newProgramCounter));
		
		int nextinst = m.getMemoryAt(newProgramCounter);
		int opcode = nextinst >>> 24;
		String mnemonic = InstructionSet.getOpcodeName(opcode);
		if (mnemonic != null)
			registerPanel.nextInstruction.setText(String.format("%s %06X", mnemonic, nextinst & 0xFFFFFF));
		else {
			registerPanel.nextInstruction.setText(String.format("Invalid (%02X)", opcode));
			registerPanel.nextInstruction.setBackground(changedColor);
		}
		
		tableModel.fireTableDataChanged();
	}
	
	
	@Override
	public void accumulatorChanged(Machine m) {
		registerPanel.accumulator.setText(String.format("%08X", m.getAccumulator() & 0xFFFFFFFFL));
		registerPanel.accumulator.setBackground(changedColor);
	}
	
	
	@Override
	public void conditionCodeChanged(Machine m) {
		registerPanel.conditionCode.setText(m.getConditionCode() ? "1" : "0");
	}
	
	
	@Override
	public void memoryChanged(Machine m, int addr) {
		tableModel.fireTableCellUpdated(addr, 2);
	}
	
	@Override
	public void programLoaded(Machine m, Program p) {
		programCounterChanged(m);
		registerPanel.programCounter.setBackground(unchangedColor);
		registerPanel.accumulator.setBackground(unchangedColor);
		registerPanel.conditionCode.setBackground(unchangedColor);
		registerPanel.nextInstruction.setBackground(unchangedColor);
		tableModel.program = p;
		tableModel.setRowCount(p.getImageSize());
	}
	
}
