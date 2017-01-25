/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import csc258comp.runner.IllegalOpcodeException;


@SuppressWarnings("serial")
final class ControlPanel extends JPanel {
	
	public ControlPanel(final DebugPanel parent) {
		super(new FlowLayout(FlowLayout.LEFT));
		Objects.requireNonNull(parent);
		final Controller controller = parent.controller;
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (controller) {
					if (controller.isRunning())
						return;
					
					parent.beginStep();
					try {
						controller.stepBack();
					} catch (IllegalOpcodeException ex) {}
					parent.endStep();
				}
			}
		});
		add(back);
		
		JButton step = new JButton("Step");
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (controller) {
					if (controller.isRunning() || controller.getMachine().isHalted())
						return;
					
					parent.beginStep();
					try {
						controller.step();
					} catch (IllegalOpcodeException ex) {}
					parent.endStep();
				}
			}
		});
		add(step);
		
		JButton resume = new JButton("Resume");
		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controller.isRunning())
					return;
				
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
		add(resume);
		
		JButton suspend = new JButton("Suspend");
		suspend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.suspend();
			}
		});
		add(suspend);
	}
	
}
