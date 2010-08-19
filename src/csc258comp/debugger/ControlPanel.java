package csc258comp.debugger;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import csc258comp.runner.Executor;
import csc258comp.runner.MachineException;


@SuppressWarnings("serial")
final class ControlPanel extends JPanel {
	
	private StatePanel parent;
	
	
	
	public ControlPanel(StatePanel parent) {
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
			if (parent.machineState.isHalted())
				return;
			try {
				parent.registerPanel.programCounter.setBackground(StatePanel.unchangedColor);
				parent.registerPanel.accumulator.setBackground(StatePanel.unchangedColor);
				parent.registerPanel.conditionCode.setBackground(StatePanel.unchangedColor);
				parent.registerPanel.nextInstruction.setBackground(StatePanel.unchangedColor);
				parent.oldConditionCode = parent.machineState.getConditionCode();
				parent.oldProgramCounter = parent.machineState.getProgramCounter();
				if (!parent.machineState.isHalted()) {
					Executor.step(parent.machineState);
					parent.stepCount++;
					parent.registerPanel.stepCountField.setText(Long.toString(parent.stepCount));
				}
			} catch (MachineException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
	
	private class RunAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			try {
				parent.machineState.removeListener(parent);
				while (!parent.machineState.isHalted()) {
					Executor.step(parent.machineState);
					parent.stepCount++;
					parent.registerPanel.stepCountField.setText(Long.toString(parent.stepCount));
					if (parent.breakpoints.contains(parent.machineState.getProgramCounter()))
						break;
				}
				parent.programCounterChanged(parent.machineState);
				parent.accumulatorChanged(parent.machineState);
				parent.conditionCodeChanged(parent.machineState);
				parent.machineState.addListener(parent);
			} catch (MachineException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
}
