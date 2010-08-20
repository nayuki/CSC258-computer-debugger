package csc258comp.debugger;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import csc258comp.runner.MachineException;


@SuppressWarnings("serial")
final class ControlPanel extends JPanel {
	
	private final DebugPanel parent;
	
	
	
	public ControlPanel(DebugPanel parent) {
		super(new FlowLayout(FlowLayout.LEFT));
		if (parent == null)
			throw new NullPointerException();
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
				parent.beginStep();
				parent.controller.step();
				parent.endStep();
			} catch (MachineException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
	
	private class RunAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			try {
				parent.beginRun();
				parent.controller.run();
				parent.endRun();
			} catch (MachineException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
}
