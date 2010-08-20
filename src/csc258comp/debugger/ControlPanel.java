package csc258comp.debugger;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import csc258comp.runner.MachineException;


@SuppressWarnings("serial")
final class ControlPanel extends JPanel {
	
	private DebugPanel parent;
	
	
	
	public ControlPanel(DebugPanel parent) {
		super(new FlowLayout(FlowLayout.LEFT));
		this.parent = parent;
		
		JButton step = new JButton("Step");
		step.addActionListener(new StepAction());
		add(step);
		
		JButton run = new JButton("Run");
		run.addActionListener(new RunAction());
		add(run);
	}
	
	
	
	private class StepAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			try {
				int oldPc = parent.machineState.getProgramCounter();
				int oldAcc = parent.machineState.getAccumulator();
				boolean oldCond = parent.machineState.getConditionCode();
				
				parent.controller.step();
				
				parent.registerPanel.programCounter.setBackground(parent.machineState.getProgramCounter() == oldPc + 1 ? DebugPanel.unchangedColor : DebugPanel.changedColor);
				parent.registerPanel.accumulator.setBackground(parent.machineState.getAccumulator() == oldAcc ? DebugPanel.unchangedColor : DebugPanel.changedColor);
				parent.registerPanel.conditionCode.setBackground(parent.machineState.getConditionCode() == oldCond ? DebugPanel.unchangedColor : DebugPanel.changedColor);
				parent.registerPanel.stepCountField.setText(Long.toString(parent.controller.getStepCount()));
			} catch (MachineException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
	
	private class RunAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			try {
				int oldAcc = parent.machineState.getAccumulator();
				boolean oldCond = parent.machineState.getConditionCode();
				
				parent.machineState.removeListener(parent);
				parent.controller.run();
				parent.programCounterChanged(parent.machineState);
				parent.accumulatorChanged(parent.machineState);
				parent.conditionCodeChanged(parent.machineState);
				parent.machineState.addListener(parent);
				
				parent.registerPanel.programCounter.setBackground(DebugPanel.unchangedColor);
				parent.registerPanel.accumulator.setBackground(parent.machineState.getAccumulator() == oldAcc ? DebugPanel.unchangedColor : DebugPanel.changedColor);
				parent.registerPanel.conditionCode.setBackground(parent.machineState.getConditionCode() == oldCond ? DebugPanel.unchangedColor : DebugPanel.changedColor);
				parent.registerPanel.stepCountField.setText(Long.toString(parent.controller.getStepCount()));
				parent.tableModel.fireTableDataChanged();
			} catch (MachineException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
}
