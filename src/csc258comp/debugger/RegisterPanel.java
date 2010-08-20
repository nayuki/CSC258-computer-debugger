package csc258comp.debugger;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

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
	
	private final DebugMachine machine;
	private final Controller controller;
	
	private int oldProgramCounter;
	private int oldAccumulator;
	private boolean oldConditionCode;
	
	private volatile Thread runThread;
	
	
	
	public RegisterPanel(DebugPanel parent) {
		super(new GridBagLayout());
		if (parent == null)
			throw new NullPointerException();
		controller = parent.controller;
		machine = parent.machine;
		
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
		programCounterChanged(false);
		accumulatorChanged(false);
		conditionCodeChanged(false);
		stepCountChanged();
	}
	
	
	public void beginStep() {
		oldProgramCounter = machine.getProgramCounter();
		oldAccumulator = machine.getAccumulator();
		oldConditionCode = machine.getConditionCode();
	}
	
	
	public void endStep() {
		programCounterChanged(true);
		accumulatorChanged(true);
		conditionCodeChanged(true);
		stepCountChanged();
	}
	
	
	public void beginRun() {
		oldAccumulator = machine.getAccumulator();
		oldConditionCode = machine.getConditionCode();
		runThread = new Thread() {
			public void run() {
				try {
					while (runThread == Thread.currentThread()) {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								updateView();
							}
						});
						Thread.sleep(100);
					}
				}
				catch (InterruptedException e) {}
				catch (InvocationTargetException e) {}
			}
		};
		runThread.start();
	}
	
	
	public void endRun() {
		Thread temp = runThread;
		runThread = null;
		temp.interrupt();
		join(temp);
		
		programCounterChanged(false);
		accumulatorChanged(true);
		conditionCodeChanged(true);
		stepCountChanged();
	}
	
	
	
	// Machine state change handlers
	
	public void programCounterChanged(boolean allowChangedBackground) {
		int newProgramCounter = machine.getProgramCounter();
		programCounter.setText(String.format("%06X", newProgramCounter));
		setChanged(programCounter, allowChangedBackground && newProgramCounter != (oldProgramCounter + 1));
		
		String nextInstText;
		if (newProgramCounter != Executor.OPSYS_ADDRESS) {
			int instWord = machine.getMemoryAt(newProgramCounter);
			int opcode = instWord >>> 24;
			int memAddr = instWord & (Machine.ADDRESS_SPACE_SIZE - 1);
			String mnemonic = InstructionSet.getOpcodeName(opcode);
			if (mnemonic != null)
				nextInstText = String.format("%s %06X", mnemonic, memAddr);
			else
				nextInstText = String.format("Invalid (%08X)", instWord);
		} else {
			nextInstText = "Halt";
		}
		nextInstruction.setText(nextInstText);
	}
	
	
	public void accumulatorChanged(boolean allowChangedBackground) {
		int newAccumulator = machine.getAccumulator();
		accumulator.setText(String.format("%08X", newAccumulator & 0xFFFFFFFFL));
		setChanged(accumulator, allowChangedBackground && newAccumulator != oldAccumulator);
	}
	
	
	public void conditionCodeChanged(boolean allowChangedBackground) {
		boolean newConditionCode = machine.getConditionCode();
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
	
	
	private static void join(Thread t) {
		while (true) {
			try {
				t.join();
				break;
			} catch (InterruptedException e) {}
		}
	}
	
}
