package csc258comp.debugger;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import csc258comp.compiler.Program;
import csc258comp.machine.impl.Executor;
import csc258comp.machine.impl.SimpleMachine;
import csc258comp.machine.model.Machine;
import csc258comp.machine.model.MachineState;
import csc258comp.machine.model.MachineStateListener;


@SuppressWarnings("serial")
class StatePanel extends JPanel implements MachineStateListener {
	
	private static final Color unchangedColor = Color.WHITE;
	private static final Color changedColor = new Color(1.0f, 1.0f, 0.5f);
	
	
	private ProbedMachineState machineState;
	
	private Set<Integer> breakpoints;
	
	private Machine machine = new SimpleMachine(System.in, System.out);
	
	private final JTextField programCounter;
	private final JTextField accumulator;
	private final JTextField conditionCode;
	private final JTextField nextInstruction;
	
	private MachineStateTableModel tableModel;
	

	private int oldProgramCounter;
	
	private boolean oldConditionCode;
	
	
	
	public StatePanel(final ProbedMachineState machineState) {
		this.machineState = machineState;
		breakpoints = new HashSet<Integer>();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		{
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints g = new GridBagConstraints();
			g.anchor = GridBagConstraints.FIRST_LINE_START;
			g.insets = new Insets(4, 4, 4, 4);
			g.fill = GridBagConstraints.HORIZONTAL;
			
			g.gridx = 0;
			g.gridy = 0;
			panel.add(new JLabel("Program counter"), g);
			programCounter = new JTextField();
			programCounter.setEditable(false);
			programCounter.setHorizontalAlignment(SwingConstants.RIGHT);
			programCounter.setBackground(unchangedColor);
			g.gridy = 1;
			panel.add(programCounter, g);
			
			g.gridx = 1;
			g.gridy = 0;
			panel.add(new JLabel("Accumulator"), g);
			accumulator = new JTextField();
			accumulator.setEditable(false);
			accumulator.setHorizontalAlignment(SwingConstants.RIGHT);
			accumulator.setBackground(unchangedColor);
			g.gridy = 1;
			panel.add(accumulator, g);
			
			g.gridx = 2;
			g.gridy = 0;
			panel.add(new JLabel("Condition code"), g);
			conditionCode = new JTextField();
			conditionCode.setEditable(false);
			conditionCode.setHorizontalAlignment(SwingConstants.RIGHT);
			conditionCode.setBackground(unchangedColor);
			g.gridy = 1;
			panel.add(conditionCode, g);
			
			g.gridx = 3;
			g.gridy = 0;
			panel.add(new JLabel("Next instruction"), g);
			nextInstruction = new JTextField();
			nextInstruction.setEditable(false);
			nextInstruction.setHorizontalAlignment(SwingConstants.LEADING);
			nextInstruction.setBackground(unchangedColor);
			g.gridy = 1;
			panel.add(nextInstruction, g);
			
			g.gridx = 4;
			g.gridy = 0;
			g.gridheight = 2;
			g.weightx = 1;
			panel.add(Box.createHorizontalGlue(), g);
			
			panel.setAlignmentX(Component.LEFT_ALIGNMENT);
			add(panel);
		}
		
		{
			Box box = Box.createHorizontalBox();
			
			JButton step = new JButton("Step");
			step.addActionListener(new StepAction());
			box.add(step);
			
			JButton run = new JButton("Run");
			run.addActionListener(new RunAction());
			box.add(run);
			
			add(box);
		}
		
		tableModel = new MachineStateTableModel(machineState, breakpoints);
		JTable table = new JTable(tableModel);
		table.setColumnSelectionAllowed(true);
		JScrollPane scrollpane = new JScrollPane(table);
		add(scrollpane);
		
		programCounterChanged(machineState);
		accumulatorChanged(machineState);
		conditionCodeChanged(machineState);
		
		machineState.addListener(this);
	}
	
	
	@Override
	public void haltedChanged(MachineState m) {}
	
	
	@Override
	public void programCounterChanged(MachineState m) {
		int newProgramCounter = m.getProgramCounter();
		programCounter.setText(String.format("%06X", newProgramCounter));
		if (newProgramCounter != ((oldProgramCounter + 1) & 0xFFFFFF))
			programCounter.setBackground(changedColor);
		
		int nextinst = m.getMemoryAt(newProgramCounter);
		int opcode = nextinst >>> 24;
		String mnemonic = Program.getOpcodeName(opcode);
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
	public void accumulatorChanged(MachineState m) {
		accumulator.setText(String.format("%08X", m.getAccumulator() & 0xFFFFFFFFL));
		accumulator.setBackground(changedColor);
	}
	
	
	@Override
	public void conditionCodeChanged(MachineState m) {
		conditionCode.setText(m.getConditionCode() ? "1" : "0");
		if (m.getConditionCode() != oldConditionCode)
			conditionCode.setBackground(changedColor);
	}
	
	
	@Override
	public void memoryChanged(MachineState m, int addr) {
		tableModel.fireTableCellUpdated(addr, 2);
	}
	
	@Override
	public void programLoaded(MachineState m, Program p) {
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
				Executor.step(machine);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
	
	
	private class RunAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			try {
				do {
					if (machineState.isHalted())
						return;
					Executor.step(machine);
				} while (!breakpoints.contains(machineState.getProgramCounter()));
				programCounter.setBackground(unchangedColor);
				accumulator.setBackground(unchangedColor);
				conditionCode.setBackground(unchangedColor);
				nextInstruction.setBackground(unchangedColor);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
}
