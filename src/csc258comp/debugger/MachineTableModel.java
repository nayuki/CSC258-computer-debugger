package csc258comp.debugger;

import java.util.Set;

import javax.swing.table.AbstractTableModel;

import csc258comp.runner.Machine;
import csc258comp.runner.Program;


@SuppressWarnings("serial")
final class MachineTableModel extends AbstractTableModel implements MachineListener {
	
	private static Class<?>[] columnClasses = {Boolean.class, String.class, String.class, String.class};
	
	private static String[] columnNames = {"Breakpoint", "Address", "Contents", "Source code"};
	
	
	
	private final DebugMachine machine;
	private final Controller controller;
	
	private final Set<Integer> breakpoints;
	
	public Program program;
	
	private int oldProgramCounter;
	
	
	
	public MachineTableModel(DebugPanel parent, Program p) {
		if (parent == null || p == null)
			throw new NullPointerException();
		machine = parent.machine;
		controller = parent.controller;
		program = p;
		
		breakpoints = controller.getBreakpoints();
		machine.addListener(this);
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
		switch (col) {
			case 0:
				return breakpoints.contains(row);
				
			case 1:
				return String.format("%08X%s", row, row == machine.getProgramCounter() ? " <=" : "");
				
			case 2:
				return String.format("%08X", machine.getMemoryAt(row));
				
			case 3:
				if (program != null) {
					String source = program.getSourceLine(row);
					if (source != null)
						return source;
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
	
	public void beginStep() {
		oldProgramCounter = machine.getProgramCounter();
	}
	
	
	public void beginRun() {
		machine.removeListener(this);
	}
	
	
	public void endStep() {
		int newProgramCounter = machine.getProgramCounter();
		fireTableCellUpdated(oldProgramCounter, 1);
		fireTableCellUpdated(newProgramCounter, 1);
	}
	
	
	public void endRun() {
		machine.addListener(this);
		fireTableDataChanged();
	}
	
	
	
	// Machine state change handlers
	
	@Override
	public void memoryChanged(Machine m, int addr) {
		fireTableCellUpdated(addr, 2);
	}
	
	
	@Override
	public void programCounterChanged(Machine m) {}
	
	@Override
	public void haltedChanged(Machine m) {}
	
	@Override
	public void accumulatorChanged(Machine m) {}
	
	@Override
	public void conditionCodeChanged(Machine m) {}
	
}
