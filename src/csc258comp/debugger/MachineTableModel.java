package csc258comp.debugger;

import java.util.Set;

import javax.swing.table.AbstractTableModel;

import csc258comp.runner.Machine;
import csc258comp.runner.Program;


@SuppressWarnings("serial")
final class MachineTableModel extends AbstractTableModel implements MachineListener {
	
	private static Class<?>[] columnClasses = {Boolean.class, String.class, String.class, String.class};
	
	private static String[] columnNames = {"Breakpoint", "Address", "Contents", "Source code"};
	
	
	
	private DebugPanel parent;
	
	private int rowCount;
	
	private Set<Integer> breakpoints;
	
	public Program program;
	
	
	
	public MachineTableModel(DebugPanel parent) {
		if (parent == null)
			throw new NullPointerException();
		this.parent = parent;
		rowCount = 0;
		breakpoints = parent.controller.getBreakpoints();
		parent.machineState.addListener(this);
	}
	
	
	
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
		return rowCount;
	}
	
	
	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return breakpoints.contains(row);
				
			case 1:
				return String.format("%08X%s", row, row == parent.machineState.getProgramCounter() ? " <=" : "");
				
			case 2:
				return String.format("%08X", parent.machineState.getMemoryAt(row));
				
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
			parent.controller.addBreakpoint(row);
		else
			parent.controller.removeBreakpoint(row);
	}
	
	
	@Override
	public void programLoaded(Machine m, Program p) {
		programCounterChanged(m);
		program = p;
		setRowCount(p.getImageSize());
	}
	
	
	@Override
	public void programCounterChanged(Machine m) {
		fireTableDataChanged();
	}
	
	
	@Override
	public void memoryChanged(Machine m, int addr) {
		fireTableCellUpdated(addr, 2);
	}
	
	
	@Override
	public void haltedChanged(Machine m) {}
	
	@Override
	public void accumulatorChanged(Machine m) {}
	
	@Override
	public void conditionCodeChanged(Machine m) {}
	
	
	private void setRowCount(int rows) {
		if (rows < 0)
			throw new IllegalArgumentException();
		this.rowCount = rows;
	}
	
}
