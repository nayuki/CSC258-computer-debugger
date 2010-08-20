package csc258comp.debugger;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import csc258comp.runner.MachineException;


@SuppressWarnings("serial")
final class ControlPanel extends JPanel {
	
	public ControlPanel(final DebugPanel parent) {
		super(new FlowLayout(FlowLayout.LEFT));
		if (parent == null)
			throw new NullPointerException();
		final Controller controller = parent.controller;
		
		JButton step = new JButton("Step");
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!controller.isRunning()) {
					try {
						parent.beginStep();
						controller.step();
						parent.endStep();
					} catch (MachineException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		});
		add(step);
		
		JButton run = new JButton("Run");
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.beginRun();
				new Thread("CSC258 debugger runner") {
					public void run() {
						controller.run();
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									parent.endRun();
								}
							});
						}
						catch (InvocationTargetException e) {}
						catch (InterruptedException e) {}
					}
				}.start();
			}
		});
		add(run);
	}
	
}
