/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import csc258comp.runner.Executor;
import csc258comp.runner.InstructionSet;
import csc258comp.runner.Machine;


@SuppressWarnings("serial")
final class RegisterPanel extends JPanel {
	
	public final JTextField stepCount;
	public final JTextField programCounter;
	public final JTextField accumulator;
	public final JTextField conditionCode;
	public final JTextField nextInstruction;
	
	private final Controller controller;
	
	private int oldProgramCounter;
	private int oldAccumulator;
	private boolean oldConditionCode;
	
	private volatile Thread updateThread;
	private final Semaphore threadStopRequest;
	
	
	
	public RegisterPanel(DebugPanel parent) {
		super(new GridBagLayout());
		Objects.requireNonNull(parent);
		controller = parent.controller;
		
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.FIRST_LINE_START;
		g.insets = new Insets(4, 4, 4, 4);
		g.fill = GridBagConstraints.HORIZONTAL;
		
		g.gridx = 0;
		g.gridy = 0;
		add(new JLabel("Step count"), g);
		g.gridy = 1;
		stepCount = newJTextField(SwingConstants.RIGHT);
		add(stepCount, g);
		
		g.gridx = 1;
		g.gridy = 0;
		add(new JLabel("Program counter"), g);
		g.gridy = 1;
		programCounter = newJTextField(SwingConstants.RIGHT);
		add(programCounter, g);
		
		g.gridx = 2;
		g.gridy = 0;
		add(new JLabel("Accumulator"), g);
		g.gridy = 1;
		accumulator = newJTextField(SwingConstants.RIGHT);
		add(accumulator, g);
		
		g.gridx = 3;
		g.gridy = 0;
		add(new JLabel("Condition code"), g);
		g.gridy = 1;
		conditionCode = newJTextField(SwingConstants.RIGHT);
		add(conditionCode, g);
		
		g.gridx = 4;
		g.gridy = 0;
		add(new JLabel("Next instruction"), g);
		g.gridy = 1;
		nextInstruction = newJTextField(SwingConstants.LEADING);
		add(nextInstruction, g);
		
		updateView();
		
		threadStopRequest = new Semaphore(0);
	}
	
	
	private static JTextField newJTextField(int horizAlign) {
		JTextField result = new JTextField();
		result.setEditable(false);
		result.setHorizontalAlignment(horizAlign);
		result.setBackground(DebugPanel.UNCHANGED_COLOR);
		result.setFont(DebugPanel.monospacedFont);
		return result;
	}
	
	
	
	// Execution handlers
	
	public void updateView() {
		Machine m = controller.getMachine();
		synchronized (m) {
			programCounterChanged(m, false);
			accumulatorChanged(m, false);
			conditionCodeChanged(m, false);
		}
		stepCountChanged();
	}
	
	
	public void beginStep() {
		Machine m = controller.getMachine();
		oldProgramCounter = m.getProgramCounter();
		oldAccumulator = m.getAccumulator();
		oldConditionCode = m.getConditionCode();
	}
	
	
	public void endStep() {
		Machine m = controller.getMachine();
		programCounterChanged(m, true);
		accumulatorChanged(m, true);
		conditionCodeChanged(m, true);
		stepCountChanged();
	}
	
	
	public void beginRun() {
		Machine m = controller.getMachine();
		oldAccumulator = m.getAccumulator();
		oldConditionCode = m.getConditionCode();
		updateThread = new Thread("CSC258 debugger UI register updater") {
			public void run() {
				try {
					do {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								updateView();
							}
						});
					} while (!threadStopRequest.tryAcquire(100, TimeUnit.MILLISECONDS));
					
					updateThread = null;
					
					Machine m = controller.getMachine();
					programCounterChanged(m, false);
					accumulatorChanged(m, false);
					conditionCodeChanged(m, false);
					stepCountChanged();
					
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				} catch (InvocationTargetException e) {
					throw new AssertionError(e);
				}
			}
		};
		updateThread.start();
	}
	
	
	public void endRun() {
		threadStopRequest.release();
	}
	
	
	
	// Machine state change handlers
	
	public void programCounterChanged(Machine m, boolean allowChangedBackground) {
		int newProgramCounter = m.getProgramCounter();
		programCounter.setText(String.format("%06X", newProgramCounter));
		setChanged(programCounter, allowChangedBackground && newProgramCounter != (oldProgramCounter + 1));
		
		String nextInstText;
		if (newProgramCounter != Executor.OPSYS_ADDRESS) {
			int instWord = m.getMemoryAt(newProgramCounter);
			int opcode = instWord >>> 24;
			int memAddr = instWord & (Machine.ADDRESS_SPACE_SIZE - 1);
			String mnemonic = InstructionSet.getMnemonic(opcode);
			if (mnemonic != null)
				nextInstText = String.format("%s %06X", mnemonic, memAddr);
			else
				nextInstText = String.format("Illegal (%02X) %06X", opcode, memAddr);
		} else {
			nextInstText = "Halted";
		}
		nextInstruction.setText(nextInstText);
	}
	
	
	public void accumulatorChanged(Machine m, boolean allowChangedBackground) {
		int newAccumulator = m.getAccumulator();
		accumulator.setText(String.format("%08X", newAccumulator & 0xFFFFFFFFL));
		setChanged(accumulator, allowChangedBackground && newAccumulator != oldAccumulator);
	}
	
	
	public void conditionCodeChanged(Machine m, boolean allowChangedBackground) {
		boolean newConditionCode = m.getConditionCode();
		conditionCode.setText(newConditionCode ? "1" : "0");
		setChanged(conditionCode, allowChangedBackground && newConditionCode != oldConditionCode);
	}
	
	
	public void stepCountChanged() {
		stepCount.setText(Long.toString(controller.getStepCount()));
	}
	
	
	
	private static void setChanged(JTextField field, boolean changed) {
		field.setBackground(getColor(changed));
	}
	
	
	private static Color getColor(boolean changed) {
		if (changed)
			return DebugPanel.CHANGED_COLOR;
		else
			return DebugPanel.UNCHANGED_COLOR;
	}
	
}
