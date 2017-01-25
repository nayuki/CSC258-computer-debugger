/* 
 * CSC258 computer
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/csc258-computer-debugger
 */

package csc258comp.debugger;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import csc258comp.runner.Machine;
import csc258comp.runner.Program;


@SuppressWarnings("serial")
final class MachineTableModel extends AbstractTableModel {
	
	private static Class<?>[] columnClasses = {Boolean.class, String.class, String.class, String.class};
	
	private static String[] columnNames = {"Breakpoint", "Address", "Contents", "Source code"};
	
	
	
	private final Controller controller;
	
	private final Set<Integer> breakpoints;
	
	public Program program;
	
	private volatile Thread updateThread;
	private final Semaphore threadStopRequest;
	
	
	
	public MachineTableModel(DebugPanel parent, Program p) {
		Objects.requireNonNull(parent);
		Objects.requireNonNull(p);
		controller = parent.controller;
		program = p;
		threadStopRequest = new Semaphore(0);
		
		breakpoints = controller.getBreakpoints();
	}
	
	
	
	// Table model methods
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	
	@Override
	public Class<?> getColumnClass(int col) {
		return columnClasses[col];
	}
	
	
	@Override
	public int getRowCount() {
		return program.getImageSize();
	}
	
	
	@Override
	public Object getValueAt(int row, int col) {
		Machine m = controller.getMachine();
		switch (col) {
			case 0:
				return breakpoints.contains(row);
				
			case 1:
				return String.format("%08X%s", row, row == m.getProgramCounter() ? " <=" : "");
				
			case 2:
				return String.format("%08X", m.getMemoryAt(row));
				
			case 3:
				if (program != null) {
					String source = program.getSourceLine(row);
					if (source != null)
						return source.replace("\t", "    ");
					else
						return "";
				}
				
			default:
				throw new AssertionError();
		}
	}
	
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return col == 0;
	}
	
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		if (col != 0)
			throw new AssertionError();
		if (!(value instanceof Boolean))
			throw new AssertionError();
		
		if (((Boolean)value).booleanValue())
			controller.addBreakpoint(row);
		else
			controller.removeBreakpoint(row);
	}
	
	
	
	// Execution handlers
	
	public void updateView() {
		synchronized (controller.getMachine()) {
			fireTableDataChanged();
		}
	}
	
	
	public void beginStep() {}
	
	
	public void endStep() {
		fireTableDataChanged();
	}
	
	
	public void beginRun() {
		updateThread = new Thread("CSC258 debugger UI table updater") {
			public void run() {
				try {
					do {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								updateView();
							}
						});
					} while (!threadStopRequest.tryAcquire(1000, TimeUnit.MILLISECONDS));
					
					updateThread = null;
					fireTableDataChanged();
					
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
	
}
