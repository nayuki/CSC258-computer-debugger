package csc258comp.debugger;

import java.util.Set;

import javax.swing.table.AbstractTableModel;

import csc258comp.compiler.Program;
import csc258comp.machine.model.MachineState;


@SuppressWarnings("serial")
class MachineStateTableModel extends AbstractTableModel {
	
	private static Class<?>[] columnClasses = {Boolean.class, String.class, String.class, String.class};
	
	private static String[] columnNames = {"Breakpoint", "Address", "Contents", "Source code"};
	
	
	
	private MachineState machineState;
	
	private int rowCount;
	
	public Program program;
	
	private Set<Integer> breakpoints;
	
	
	
	public MachineStateTableModel(MachineState m, Set<Integer> breakpoints) {
		machineState = m;
		rowCount = 0;
		this.breakpoints = breakpoints;
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
				return String.format("%08X%s", row, row == machineState.getProgramCounter() ? " <=" : "");
			
			case 2:
				return String.format("%08X", machineState.getMemoryAt(row));
			
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
			breakpoints.add(row);
		else
			breakpoints.remove(row);
	}
	
	
	void setRowCount(int rows) {
		if (rows < 0)
			throw new IllegalArgumentException();
		this.rowCount = rows;
	}
	
}
